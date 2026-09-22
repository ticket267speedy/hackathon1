package com.tuckersoft.branchengine.listener;
import com.tuckersoft.branchengine.event.DecisionEvent;
import com.tuckersoft.branchengine.model.*;
import com.tuckersoft.branchengine.repository.*;
import com.tuckersoft.branchengine.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import java.time.Instant;
@Component @RequiredArgsConstructor @Slf4j
public class RealityLogListener {
    private final RealityLogRepository logRepo;
    private final UserRepository userRepo;
    private final EmailService emailService;
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onDecision(DecisionEvent ev) {
        RealityLog entry = RealityLog.builder()
                .decisionId(ev.decisionId()).userId(ev.userId()).nodeKey(ev.nodeKey())
                .branchType(ev.branchType()).impactType(ev.impactType())
                .impactLabel(ev.impactLabel()).delivered(false).build();
        logRepo.save(entry);
        String to = userRepo.findById(ev.userId()).map(User::getEmail).orElse("qa@tuckersoft.test");
        String subject = "[BRANCH-DEC] decision " + ev.decisionId() + " on " + ev.nodeKey();
        String body = """
                Decision ID   : %d
                Node          : %s
                BranchType    : %s
                Impact        : %s
                ImpactLabel   : %s
                """.formatted(ev.decisionId(), ev.nodeKey(), ev.branchType(), ev.impactType(), ev.impactLabel());
        try {
            emailService.send(to, subject, body);
            entry.setDelivered(true); entry.setSentAt(Instant.now());
        } catch (Exception ex) {
            log.warn("Mail failed, decision={} err={}", ev.decisionId(), ex.getMessage());
            entry.setDelivered(false); entry.setErrorMessage(ex.getMessage());
        }
        logRepo.save(entry);
        log.info("[BRANCH-DEC] decisionId={} user={} node={} branch={} impact={}",
                ev.decisionId(), ev.userId(), ev.nodeKey(), ev.branchType(), ev.impactType());
    }
}
