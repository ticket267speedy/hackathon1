package com.ticket267.hackathon1.repository;
import com.ticket267.hackathon1.model.NodeType;
import com.ticket267.hackathon1.model.WorkflowNode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WorkflowNodeRepository extends JpaRepository<WorkflowNode, Long> {
    Optional<WorkflowNode> findByNodeKey(String nodeKey);
    boolean existsByNodeKey(String nodeKey);
    Optional<WorkflowNode> findFirstByIsStartTrue();
    List<WorkflowNode> findByNodeType(NodeType nodeType);
}
