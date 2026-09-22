package com.tuckersoft.branchengine.repository;
import com.tuckersoft.branchengine.model.Decision;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface DecisionRepository extends JpaRepository<Decision, Long> {
    List<Decision> findByPlaythroughIdOrderByDecidedAtAsc(Long playthroughId);
    long countByPlaythroughId(Long playthroughId);
    Optional<Decision> findByIdempotencyKey(String idempotencyKey);
}
