package com.tuckersoft.branchengine.controller;
import com.tuckersoft.branchengine.dto.DecisionResponse;
import com.tuckersoft.branchengine.service.DecisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/decisions") @RequiredArgsConstructor
public class DecisionController {
    private final DecisionService svc;
    private boolean adm(Authentication a){return a.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(x -> x.equals("ROLE_ADMIN"));}
    @GetMapping("/{id}") public ResponseEntity<DecisionResponse> get(@PathVariable Long id, Authentication a){
        return ResponseEntity.ok(svc.byId(id,a.getName(),adm(a)));
    }
}
