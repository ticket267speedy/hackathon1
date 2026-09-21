package com.ticket267.hackathon1.controller;

import com.ticket267.hackathon1.dto.PlayerResponse;
import com.ticket267.hackathon1.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PlayerService service;

    @GetMapping("/players")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PlayerResponse>> list() {
        return ResponseEntity.ok(service.listAll());
    }
}
