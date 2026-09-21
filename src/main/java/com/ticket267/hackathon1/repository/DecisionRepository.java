package com.ticket267.hackathon1.repository;
import com.ticket267.hackathon1.model.Decision;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DecisionRepository extends JpaRepository<Decision, Long> {
    List<Decision> findByPlaythroughIdOrderByDecidedAtAsc(Long playthroughId);
    long countByPlaythroughId(Long playthroughId);
}
