package com.ticket267.hackathon1.service;

import com.ticket267.hackathon1.dto.*;
import com.ticket267.hackathon1.exception.ConflictException;
import com.ticket267.hackathon1.exception.UnauthorizedException;
import com.ticket267.hackathon1.model.Player;
import com.ticket267.hackathon1.model.RefreshToken;
import com.ticket267.hackathon1.model.Role;
import com.ticket267.hackathon1.repository.PlayerRepository;
import com.ticket267.hackathon1.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PlayerRepository playerRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshService;

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (playerRepo.existsByUsername(req.getUsername()))
            throw new ConflictException("Username ya existe");
        if (playerRepo.existsByEmail(req.getEmail()))
            throw new ConflictException("Email ya existe");

        Player p = Player.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .passwordHash(encoder.encode(req.getPassword()))
                .role(Role.USER)
                .build();
        playerRepo.save(p);
        return issueTokens(p);
    }

    @Transactional
    public AuthResponse login(LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        Player p = playerRepo.findByUsername(req.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Credenciales inválidas"));
        p.setLastLogin(LocalDateTime.now());
        playerRepo.save(p);
        return issueTokens(p);
    }

    @Transactional
    public AuthResponse refresh(RefreshRequest req) {
        RefreshToken rt = refreshService.verify(req.getRefreshToken());
        // rotación: revocamos el anterior y emitimos uno nuevo
        rt.setRevoked(true);
        Player p = rt.getPlayer();
        return issueTokens(p);
    }

    @Transactional
    public void logout(RefreshRequest req) {
        refreshService.revoke(req.getRefreshToken());
    }

    private AuthResponse issueTokens(Player p) {
        String access = jwtUtil.generateAccessToken(p.getUsername(), List.of("ROLE_" + p.getRole().name()));
        RefreshToken rt = refreshService.create(p);
        return AuthResponse.builder()
                .accessToken(access)
                .refreshToken(rt.getToken())
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getAccessExpMs() / 1000)
                .player(PlayerResponse.from(p))
                .build();
    }
}
