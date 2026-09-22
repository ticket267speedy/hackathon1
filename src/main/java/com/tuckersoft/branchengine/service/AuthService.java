package com.tuckersoft.branchengine.service;
import com.tuckersoft.branchengine.dto.*;
import com.tuckersoft.branchengine.exception.*;
import com.tuckersoft.branchengine.model.*;
import com.tuckersoft.branchengine.repository.UserRepository;
import com.tuckersoft.branchengine.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
@Service @RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepo; private final PasswordEncoder encoder;
    private final AuthenticationManager authManager; private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshService;
    @Transactional public AuthResponse register(RegisterRequest r) {
        if (userRepo.existsByUsername(r.getUsername())) throw new ConflictException("Username ya existe");
        if (userRepo.existsByEmail(r.getEmail())) throw new ConflictException("Email ya existe");
        User u = User.builder().username(r.getUsername()).email(r.getEmail())
                .passwordHash(encoder.encode(r.getPassword())).role(Role.USER).build();
        userRepo.save(u); return issue(u);
    }
    @Transactional public AuthResponse login(LoginRequest r) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(r.getUsername(), r.getPassword()));
        User u = userRepo.findByUsername(r.getUsername()).orElseThrow(() -> new UnauthorizedException("Credenciales invalidas"));
        u.setLastLogin(Instant.now()); userRepo.save(u); return issue(u);
    }
    @Transactional public AuthResponse refresh(RefreshRequest r) {
        RefreshToken rt = refreshService.verify(r.getRefreshToken()); rt.setRevoked(true); return issue(rt.getUser());
    }
    @Transactional public void logout(RefreshRequest r) { refreshService.revoke(r.getRefreshToken()); }
    private AuthResponse issue(User u) {
        String access = jwtUtil.generateAccessToken(u.getUsername(), List.of("ROLE_" + u.getRole().name()));
        RefreshToken rt = refreshService.create(u);
        return AuthResponse.builder().accessToken(access).refreshToken(rt.getToken())
                .tokenType("Bearer").expiresIn(jwtUtil.getAccessMs() / 1000).user(UserResponse.from(u)).build();
    }
}
