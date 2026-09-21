package com.ticket267.hackathon1.service;

import com.ticket267.hackathon1.exception.UnauthorizedException;
import com.ticket267.hackathon1.model.Player;
import com.ticket267.hackathon1.model.RefreshToken;
import com.ticket267.hackathon1.repository.RefreshTokenRepository;
import com.ticket267.hackathon1.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repo;
    private final JwtUtil jwtUtil;

    @Transactional
    public RefreshToken create(Player player) {
        String token = jwtUtil.generateRefreshToken(player.getUsername());
        RefreshToken rt = RefreshToken.builder()
                .token(token)
                .player(player)
                .expiresAt(LocalDateTime.now().plusSeconds(jwtUtil.getRefreshExpMs() / 1000))
                .revoked(false)
                .build();
        return repo.save(rt);
    }

    public RefreshToken verify(String token) {
        RefreshToken rt = repo.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException("Refresh token inválido"));
        if (Boolean.TRUE.equals(rt.getRevoked()))
            throw new UnauthorizedException("Refresh token revocado");
        if (rt.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new UnauthorizedException("Refresh token expirado");
        if (!jwtUtil.isValid(token) || !"refresh".equals(jwtUtil.getType(token)))
            throw new UnauthorizedException("Refresh token inválido");
        return rt;
    }

    @Transactional
    public void revoke(String token) {
        repo.findByToken(token).ifPresent(rt -> { rt.setRevoked(true); repo.save(rt); });
    }

    @Transactional
    public void revokeAll(Player player) {
        repo.deleteByPlayerId(player.getId());
    }
}
