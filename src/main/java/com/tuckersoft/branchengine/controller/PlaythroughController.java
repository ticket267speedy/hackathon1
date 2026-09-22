package com.tuckersoft.branchengine.controller;
import com.tuckersoft.branchengine.dto.*;
import com.tuckersoft.branchengine.model.Decision;
import com.tuckersoft.branchengine.model.RealityLog;
import com.tuckersoft.branchengine.repository.DecisionRepository;
import com.tuckersoft.branchengine.repository.RealityLogRepository;
import com.tuckersoft.branchengine.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/v1/playthroughs") @RequiredArgsConstructor
public class PlaythroughController {
    private final PlaythroughService svc;
    private final DecisionService decisionSvc;
    private final DecisionRepository decisionRepo;
    private final RealityLogRepository logRepo;
    private boolean adm(Authentication a){return a.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(x -> x.equals("ROLE_ADMIN"));}
    @PostMapping public ResponseEntity<PlaythroughResponse> c(Authentication a,@Valid @RequestBody PlaythroughRequest r){
        return ResponseEntity.status(HttpStatus.CREATED).body(svc.create(r,a.getName(),adm(a)));
    }
    @GetMapping public ResponseEntity<List<PlaythroughResponse>> list(Authentication a){return ResponseEntity.ok(svc.list(a.getName(),adm(a)));}
    @GetMapping("/{id}") public ResponseEntity<PlaythroughResponse> get(@PathVariable Long id, Authentication a){return ResponseEntity.ok(svc.get(id,a.getName(),adm(a)));}
    @PostMapping("/{id}/decisions")
    public ResponseEntity<DecisionAcceptedResponse> decide(@PathVariable Long id, Authentication a,
            @RequestHeader(value="Idempotency-Key",required=false) String idem,
            @Valid @RequestBody DecisionRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(decisionSvc.decide(id,r,a.getName(),adm(a),idem));
    }
    @GetMapping("/{id}/decisions")
    public ResponseEntity<List<DecisionResponse>> history(@PathVariable Long id, Authentication a){
        return ResponseEntity.ok(decisionSvc.history(id,a.getName(),adm(a)));
    }
    @GetMapping("/{id}/logs")
    public ResponseEntity<List<RealityLogResponse>> logs(@PathVariable Long id, Authentication a){
        // valida ownership llamando al servicio
        svc.get(id, a.getName(), adm(a));
        List<Long> ids = decisionRepo.findByPlaythroughIdOrderByDecidedAtAsc(id).stream().map(Decision::getId).toList();
        List<RealityLogResponse> out = ids.isEmpty() ? List.of()
                : logRepo.findByDecisionIdInOrderByCreatedAtAsc(ids).stream().map(RealityLogResponse::from).toList();
        return ResponseEntity.ok(out);
    }
}
