package com.tuckersoft.branchengine.service;
import com.tuckersoft.branchengine.exception.UnauthorizedException;
import com.tuckersoft.branchengine.model.*;
import com.tuckersoft.branchengine.repository.RefreshTokenRepository;
import com.tuckersoft.branchengine.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
@Service @RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository repo; private final JwtUtil jwt;
    @Transactional public RefreshToken create(User u) {
        String t = jwt.generateRefreshToken(u.getUsername());
        return repo.save(RefreshToken.builder().token(t).user(u)
                .expiresAt(Instant.now().plusMillis(jwt.getRefreshMs())).revoked(false).build());
    }
    public RefreshToken verify(String t) {
        RefreshToken rt = repo.findByToken(t).orElseThrow(() -> new UnauthorizedException("Refresh token invalido"));
        if (Boolean.TRUE.equals(rt.getRevoked())) throw new UnauthorizedException("Refresh token revocado");
        if (rt.getExpiresAt().isBefore(Instant.now())) throw new UnauthorizedException("Refresh token expirado");
        if (!jwt.isValid(t) || !"refresh".equals(jwt.getType(t))) throw new UnauthorizedException("Refresh token invalido");
        return rt;
    }
    @Transactional public void revoke(String t) { repo.findByToken(t).ifPresent(r -> { r.setRevoked(true); repo.save(r); }); }
}
