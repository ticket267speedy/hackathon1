package com.tuckersoft.branchengine.repository;
import com.tuckersoft.branchengine.model.BranchType;
import com.tuckersoft.branchengine.model.StoryNode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface StoryNodeRepository extends JpaRepository<StoryNode, Long> {
    Optional<StoryNode> findByNodeKey(String nodeKey);
    boolean existsByNodeKey(String nodeKey);
    List<StoryNode> findByBranchType(BranchType branchType);
}
