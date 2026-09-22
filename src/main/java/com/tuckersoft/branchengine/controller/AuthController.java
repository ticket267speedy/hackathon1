package com.tuckersoft.branchengine.controller;
import com.tuckersoft.branchengine.dto.*;
import com.tuckersoft.branchengine.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/auth") @RequiredArgsConstructor
public class AuthController {
    private final AuthService svc;
    @PostMapping("/register") public ResponseEntity<AuthResponse> r(@Valid @RequestBody RegisterRequest x){return ResponseEntity.status(HttpStatus.CREATED).body(svc.register(x));}
    @PostMapping("/login")    public ResponseEntity<AuthResponse> l(@Valid @RequestBody LoginRequest x){return ResponseEntity.ok(svc.login(x));}
    @PostMapping("/refresh")  public ResponseEntity<AuthResponse> rf(@Valid @RequestBody RefreshRequest x){return ResponseEntity.ok(svc.refresh(x));}
    @PostMapping("/logout")   public ResponseEntity<Void> lo(@Valid @RequestBody RefreshRequest x){svc.logout(x);return ResponseEntity.noContent().build();}
}
