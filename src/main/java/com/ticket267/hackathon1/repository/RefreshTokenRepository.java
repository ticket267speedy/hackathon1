package com.ticket267.hackathon1.repository;
import com.ticket267.hackathon1.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByPlayerId(Long playerId);
}
