package com.ticket267.hackathon1.repository;
import com.ticket267.hackathon1.model.Playthrough;
import com.ticket267.hackathon1.model.PlaythroughStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PlaythroughRepository extends JpaRepository<Playthrough, Long> {
    List<Playthrough> findByPlayerIdOrderByStartedAtDesc(Long playerId);
    Optional<Playthrough> findByIdAndPlayerId(Long id, Long playerId);
    long countByStatus(PlaythroughStatus status);
}
