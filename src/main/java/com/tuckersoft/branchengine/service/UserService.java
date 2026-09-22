package com.tuckersoft.branchengine.service;
import com.tuckersoft.branchengine.dto.*;
import com.tuckersoft.branchengine.exception.*;
import com.tuckersoft.branchengine.model.User;
import com.tuckersoft.branchengine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor
public class UserService {
    private final UserRepository repo; private final PasswordEncoder encoder;
    public User get(String username) { return repo.findByUsername(username).orElseThrow(() -> new NotFoundException("Usuario no encontrado")); }
    public UserResponse me(String username) { return UserResponse.from(get(username)); }
    public UserResponse getById(Long id) { return UserResponse.from(repo.findById(id).orElseThrow(() -> new NotFoundException("Usuario no encontrado"))); }
    public List<UserResponse> list() { return repo.findAll().stream().map(UserResponse::from).toList(); }
    @Transactional public UserResponse update(String username, UpdateUserRequest r) {
        User u = get(username);
        if (r.getEmail() != null && !r.getEmail().isBlank() && !r.getEmail().equalsIgnoreCase(u.getEmail())) {
            if (repo.existsByEmail(r.getEmail())) throw new ConflictException("Email ya existe");
            u.setEmail(r.getEmail());
        }
        if (r.getNewPassword() != null && !r.getNewPassword().isBlank()) {
            if (r.getCurrentPassword() == null || r.getCurrentPassword().isBlank())
                throw new UnauthorizedException("Debes enviar currentPassword");
            if (!encoder.matches(r.getCurrentPassword(), u.getPasswordHash()))
                throw new UnauthorizedException("Password actual incorrecto");
            u.setPasswordHash(encoder.encode(r.getNewPassword()));
        }
        repo.save(u); return UserResponse.from(u);
    }
}
