package com.tuckersoft.branchengine.controller;
import com.tuckersoft.branchengine.dto.*;
import com.tuckersoft.branchengine.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/v1/users") @RequiredArgsConstructor
public class UserController {
    private final UserService svc;
    @GetMapping("/me") public ResponseEntity<UserResponse> me(Authentication a){return ResponseEntity.ok(svc.me(a.getName()));}
    @PatchMapping("/me") public ResponseEntity<UserResponse> upd(Authentication a,@Valid @RequestBody UpdateUserRequest r){return ResponseEntity.ok(svc.update(a.getName(),r));}
    @GetMapping @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> list(){return ResponseEntity.ok(svc.list());}
    @GetMapping("/{id}") public ResponseEntity<UserResponse> byId(@PathVariable Long id, Authentication a){
        boolean admin = a.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals("ROLE_ADMIN"));
        if (!admin && !svc.me(a.getName()).getId().equals(id)) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(svc.getById(id));
    }
}
