package com.ticket267.hackathon1.controller;

import com.ticket267.hackathon1.dto.PlaythroughRequest;
import com.ticket267.hackathon1.dto.PlaythroughResponse;
import com.ticket267.hackathon1.service.PlaythroughService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playthroughs")
@RequiredArgsConstructor
public class PlaythroughController {

    private final PlaythroughService service;

    private boolean isAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_ADMIN"));
    }

    @PostMapping
    public ResponseEntity<PlaythroughResponse> create(Authentication auth,
                                                      @Valid @RequestBody PlaythroughRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(req, auth.getName(), isAdmin(auth)));
    }

    @GetMapping
    public ResponseEntity<List<PlaythroughResponse>> list(Authentication auth) {
        return ResponseEntity.ok(service.list(auth.getName(), isAdmin(auth)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaythroughResponse> get(@PathVariable Long id, Authentication auth) {
        return ResponseEntity.ok(service.get(id, auth.getName(), isAdmin(auth)));
    }
}
