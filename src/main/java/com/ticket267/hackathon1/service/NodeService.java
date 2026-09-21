package com.ticket267.hackathon1.service;

import com.ticket267.hackathon1.dto.NodeRequest;
import com.ticket267.hackathon1.dto.NodeResponse;
import com.ticket267.hackathon1.exception.BadRequestException;
import com.ticket267.hackathon1.exception.ConflictException;
import com.ticket267.hackathon1.exception.NotFoundException;
import com.ticket267.hackathon1.model.NodeType;
import com.ticket267.hackathon1.model.WorkflowNode;
import com.ticket267.hackathon1.repository.PlaythroughRepository;
import com.ticket267.hackathon1.repository.WorkflowNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NodeService {

    private final WorkflowNodeRepository repo;
    private final PlaythroughRepository playthroughRepo;

    public List<NodeResponse> listAll() {
        return repo.findAll().stream().map(NodeResponse::from).toList();
    }

    public NodeResponse getByKey(String nodeKey) {
        WorkflowNode n = repo.findByNodeKey(nodeKey)
                .orElseThrow(() -> new NotFoundException("Nodo no encontrado: " + nodeKey));
        return NodeResponse.from(n);
    }

    @Transactional
    public NodeResponse create(NodeRequest req) {
        if (repo.existsByNodeKey(req.getNodeKey()))
            throw new ConflictException("nodeKey ya existe: " + req.getNodeKey());

        validateByType(req);

        if (req.getNodeType() == NodeType.START && repo.findFirstByIsStartTrue().isPresent())
            throw new ConflictException("Ya existe un nodo START; solo puede haber uno");

        WorkflowNode n = WorkflowNode.builder()
                .nodeKey(req.getNodeKey())
                .title(req.getTitle())
                .narrativeText(req.getNarrativeText())
                .nodeType(req.getNodeType())
                .leftOption(req.getLeftOption())
                .rightOption(req.getRightOption())
                .isStart(req.getNodeType() == NodeType.START)
                .isEnding(req.getNodeType() == NodeType.ENDING)
                .build();
        repo.save(n);
        return NodeResponse.from(n);
    }

    @Transactional
    public NodeResponse update(String nodeKey, NodeRequest req) {
        WorkflowNode n = repo.findByNodeKey(nodeKey)
                .orElseThrow(() -> new NotFoundException("Nodo no encontrado: " + nodeKey));

        if (!n.getNodeKey().equals(req.getNodeKey()) && repo.existsByNodeKey(req.getNodeKey()))
            throw new ConflictException("nodeKey ya existe: " + req.getNodeKey());

        validateByType(req);

        if (req.getNodeType() == NodeType.START && !Boolean.TRUE.equals(n.getIsStart())
                && repo.findFirstByIsStartTrue().isPresent())
            throw new ConflictException("Ya existe un nodo START; solo puede haber uno");

        n.setNodeKey(req.getNodeKey());
        n.setTitle(req.getTitle());
        n.setNarrativeText(req.getNarrativeText());
        n.setNodeType(req.getNodeType());
        n.setLeftOption(req.getLeftOption());
        n.setRightOption(req.getRightOption());
        n.setIsStart(req.getNodeType() == NodeType.START);
        n.setIsEnding(req.getNodeType() == NodeType.ENDING);
        repo.save(n);
        return NodeResponse.from(n);
    }

    @Transactional
    public void delete(String nodeKey) {
        WorkflowNode n = repo.findByNodeKey(nodeKey)
                .orElseThrow(() -> new NotFoundException("Nodo no encontrado: " + nodeKey));

        if (playthroughRepo.existsByCurrentNodeId(n.getId()))
            throw new ConflictException("No se puede borrar: hay partidas asociadas a este nodo");

        repo.delete(n);
    }

    private void validateByType(NodeRequest req) {
        if (req.getNodeType() == NodeType.DECISION) {
            if (isBlank(req.getLeftOption()) || isBlank(req.getRightOption()))
                throw new BadRequestException("Un nodo DECISION requiere leftOption y rightOption");
        }
    }

    private boolean isBlank(String s) { return s == null || s.isBlank(); }
}
