package com.ticket267.hackathon1.repository;
import com.ticket267.hackathon1.model.Analytics;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {
    Optional<Analytics> findByPlaythroughId(Long playthroughId);
}
