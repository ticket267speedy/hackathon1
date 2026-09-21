package com.ticket267.hackathon1.service;

import com.ticket267.hackathon1.dto.PlayerResponse;
import com.ticket267.hackathon1.dto.UpdatePlayerRequest;
import com.ticket267.hackathon1.exception.ConflictException;
import com.ticket267.hackathon1.exception.NotFoundException;
import com.ticket267.hackathon1.exception.UnauthorizedException;
import com.ticket267.hackathon1.model.Player;
import com.ticket267.hackathon1.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository repo;
    private final PasswordEncoder encoder;

    public Player getByUsername(String username) {
        return repo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Player no encontrado: " + username));
    }

    public PlayerResponse getMe(String username) {
        return PlayerResponse.from(getByUsername(username));
    }

    @Transactional
    public PlayerResponse updateMe(String username, UpdatePlayerRequest req) {
        Player p = getByUsername(username);

        if (req.getEmail() != null && !req.getEmail().isBlank()
                && !req.getEmail().equalsIgnoreCase(p.getEmail())) {
            if (repo.existsByEmail(req.getEmail()))
                throw new ConflictException("Email ya existe");
            p.setEmail(req.getEmail());
        }

        boolean wantsPasswordChange = req.getNewPassword() != null && !req.getNewPassword().isBlank();
        if (wantsPasswordChange) {
            if (req.getCurrentPassword() == null || req.getCurrentPassword().isBlank())
                throw new UnauthorizedException("Debes enviar currentPassword");
            if (!encoder.matches(req.getCurrentPassword(), p.getPasswordHash()))
                throw new UnauthorizedException("Password actual incorrecto");
            p.setPasswordHash(encoder.encode(req.getNewPassword()));
        }

        repo.save(p);
        return PlayerResponse.from(p);
    }

    public List<PlayerResponse> listAll() {
        return repo.findAll().stream().map(PlayerResponse::from).toList();
    }
}
