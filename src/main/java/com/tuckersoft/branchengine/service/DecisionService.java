package com.tuckersoft.branchengine.service;

import com.tuckersoft.branchengine.dto.*;
import com.tuckersoft.branchengine.event.DecisionEvent;
import com.tuckersoft.branchengine.exception.*;
import com.tuckersoft.branchengine.model.*;
import com.tuckersoft.branchengine.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service @RequiredArgsConstructor
public class DecisionService {

    private final DecisionRepository decisionRepo;
    private final PlaythroughRepository playthroughRepo;
    private final StoryNodeRepository nodeRepo;
    private final ApplicationEventPublisher publisher;

    public static BranchType classifyBranch(String text) {
        String t = text == null ? "" : text.trim().replaceAll("\\s+", " ").toLowerCase();
        if (t.equals("stay") || t.equals("demand")) return BranchType.BRANCH;
        if (t.equals("destroy the car"))              return BranchType.FREEFORM;
        if (t.equals("hit"))                          return BranchType.REFUSAL;
        if (t.equals("started"))                      return BranchType.REFUSAL;
        if (t.equals("delete the report"))            return BranchType.COMMENT;
        return BranchType.FREEFORM;
    }
    public static ImpactType classifyImpact(String text) {
        String t = text == null ? "" : text.trim().replaceAll("\\s+", " ").toLowerCase();
        if (t.equals("stay"))             return ImpactType.SECURITY;
        if (t.equals("demand"))           return ImpactType.STORY;
        if (t.equals("destroy the car"))  return ImpactType.SECURITY;
        if (t.equals("hit"))              return ImpactType.QA;
        if (t.equals("started"))          return ImpactType.HEAD_OF_IT;
        if (t.equals("delete the report"))return ImpactType.STORY;
        return ImpactType.STORY;
    }
    public static String impactLabel(ImpactType i) {
        return switch (i) {
            case SECURITY    -> "SECURITY_KEY";
            case QA          -> "TEST_PASS";
            case HEAD_OF_IT  -> "HEAD_OF_IT";
            case STORY       -> "STORY_KEY";
            case MARKETING   -> "PROMO";
        };
    }
    public static int scoreDelta(ImpactType i) {
        return switch (i) {
            case SECURITY, HEAD_OF_IT -> +10;
            case QA, STORY            -> +5;
            case MARKETING            -> 0;
        };
    }

    @Transactional
    public DecisionAcceptedResponse decide(Long playthroughId, DecisionRequest req,
                                           String authUser, boolean isAdmin, String idemKey) {
        if (idemKey != null && decisionRepo.findByIdempotencyKey(idemKey).isPresent())
            throw new ConflictException("Idempotency-Key ya usado");

        Playthrough p = playthroughRepo.findById(playthroughId)
                .orElseThrow(() -> new NotFoundException("Partida no encontrada: " + playthroughId));
        if (!isAdmin && !p.getUser().getUsername().equals(authUser))
            throw new ForbiddenException("Esta partida no es tuya");
        if (p.getStatus() == PlaythroughStatus.FINISHED)
            throw new ConflictException("La partida ya esta finalizada");

        StoryNode current = p.getCurrentNode();
        BranchType bt = classifyBranch(req.getInputText());
        ImpactType it = classifyImpact(req.getInputText());
        String label = impactLabel(it);
        int delta = scoreDelta(it);

        StoryNode next = current;
        String chosen = null;
        if (current.getBranchType() == BranchType.BRANCH) {
            if (req.getChosenOptionCode() == null || req.getChosenOptionCode().isBlank())
                throw new BadRequestException("chosenOptionCode obligatorio en BRANCH");
            chosen = req.getChosenOptionCode().toUpperCase();
            String nextKey = "LEFT".equals(chosen) ? current.getLeftOptionCode() : current.getRightOptionCode();
            if (nextKey == null || nextKey.isBlank())
                throw new BadRequestException("No hay camino definido para " + chosen);
            next = nodeRepo.findByNodeKey(nextKey)
                    .orElseThrow(() -> new BadRequestException("Nodo destino no existe: " + nextKey));
        } else if (bt == BranchType.REFUSAL) {
            next = current;
        }

        boolean timedOut = Boolean.TRUE.equals(req.getTimedOut());
        long millis = req.getResponseMillis() != null ? req.getResponseMillis() : 0L;
        if (timedOut) delta -= 5;

        Decision d = Decision.builder()
                .playthrough(p).node(current).inputText(req.getInputText())
                .branchType(bt).impactType(it).impactLabel(label)
                .chosenOptionCode(chosen).idempotencyKey(idemKey)
                .timedOut(timedOut).responseMillis(millis).build();
        decisionRepo.save(d);

        p.setCurrentNode(next);
        p.setScore(Math.max(0, p.getScore() + delta));
        p.setTotalDecisions(p.getTotalDecisions() + 1);

        if (next.getBranchType() == BranchType.ENDING) {
            p.setStatus(PlaythroughStatus.FINISHED);
            p.setFinishedAt(Instant.now());
            p.setEndingKey(next.getNodeKey());
        } else if (next.getBranchType() == BranchType.ENDING_CORRUPT) {
            p.setStatus(PlaythroughStatus.FINISHED);
            p.setFinishedAt(Instant.now());
            p.setEndingKey(next.getNodeKey());
            p.setScore(Math.max(0, p.getScore() - 10));
        }
        playthroughRepo.save(p);

        publisher.publishEvent(new DecisionEvent(d.getId(), p.getUser().getId(),
                next.getNodeKey(), bt, it, label));

        return DecisionAcceptedResponse.builder()
                .id(d.getId()).playthroughId(p.getId())
                .branchType(bt).impactLabel(label)
                .playthroughStatus(p.getStatus()).score(p.getScore())
                .currentNodeKey(next.getNodeKey()).build();
    }

    @Transactional(readOnly = true)
    public List<DecisionResponse> history(Long playthroughId, String authUser, boolean isAdmin) {
        Playthrough p = playthroughRepo.findById(playthroughId)
                .orElseThrow(() -> new NotFoundException("Partida no encontrada: " + playthroughId));
        if (!isAdmin && !p.getUser().getUsername().equals(authUser))
            throw new ForbiddenException("Esta partida no es tuya");
        return decisionRepo.findByPlaythroughIdOrderByDecidedAtAsc(playthroughId)
                .stream().map(DecisionResponse::from).toList();
    }
    @Transactional(readOnly = true)
    public DecisionResponse byId(Long id, String authUser, boolean isAdmin) {
        Decision d = decisionRepo.findById(id).orElseThrow(() -> new NotFoundException("Decision no encontrada"));
        if (!isAdmin && !d.getPlaythrough().getUser().getUsername().equals(authUser))
            throw new ForbiddenException("Esta decision no es tuya");
        return DecisionResponse.from(d);
    }
}
