package com.ticket267.hackathon1.controller;

import com.ticket267.hackathon1.dto.PlayerResponse;
import com.ticket267.hackathon1.dto.UpdatePlayerRequest;
import com.ticket267.hackathon1.service.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService service;

    @GetMapping("/me")
    public ResponseEntity<PlayerResponse> me(Authentication auth) {
        return ResponseEntity.ok(service.getMe(auth.getName()));
    }

    @PatchMapping("/me")
    public ResponseEntity<PlayerResponse> updateMe(Authentication auth,
                                                   @Valid @RequestBody UpdatePlayerRequest req) {
        return ResponseEntity.ok(service.updateMe(auth.getName(), req));
    }
}
