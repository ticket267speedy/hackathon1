package com.tuckersoft.branchengine.repository;
import com.tuckersoft.branchengine.model.Playthrough;
import com.tuckersoft.branchengine.model.PlaythroughStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface PlaythroughRepository extends JpaRepository<Playthrough, Long> {
    List<Playthrough> findByUserIdOrderByStartedAtDesc(Long userId);
    Optional<Playthrough> findByIdAndUserId(Long id, Long userId);
    long countByStatus(PlaythroughStatus status);
    boolean existsByCurrentNodeId(Long nodeId);
}
