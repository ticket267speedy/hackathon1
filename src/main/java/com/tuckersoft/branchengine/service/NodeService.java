package com.tuckersoft.branchengine.service;
import com.tuckersoft.branchengine.dto.*;
import com.tuckersoft.branchengine.exception.*;
import com.tuckersoft.branchengine.model.*;
import com.tuckersoft.branchengine.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor
public class NodeService {
    private final StoryNodeRepository repo;
    private final PlaythroughRepository playthroughRepo;
    public List<NodeResponse> list() { return repo.findAll().stream().map(NodeResponse::from).toList(); }
    public NodeResponse getByKey(String k) {
        return NodeResponse.from(repo.findByNodeKey(k).orElseThrow(() -> new NotFoundException("Nodo no encontrado: " + k)));
    }
    @Transactional public NodeResponse create(NodeRequest r) {
        if (repo.existsByNodeKey(r.getNodeKey())) throw new ConflictException("nodeKey ya existe: " + r.getNodeKey());
        validate(r);
        StoryNode n = StoryNode.builder().nodeKey(r.getNodeKey()).title(r.getTitle())
                .narrativeText(r.getNarrativeText()).branchType(r.getBranchType())
                .leftOptionCode(r.getLeftOptionCode()).rightOptionCode(r.getRightOptionCode()).build();
        repo.save(n); return NodeResponse.from(n);
    }
    @Transactional public NodeResponse update(String k, NodeRequest r) {
        StoryNode n = repo.findByNodeKey(k).orElseThrow(() -> new NotFoundException("Nodo no encontrado: " + k));
        if (!n.getNodeKey().equals(r.getNodeKey()) && repo.existsByNodeKey(r.getNodeKey()))
            throw new ConflictException("nodeKey ya existe: " + r.getNodeKey());
        validate(r);
        n.setNodeKey(r.getNodeKey()); n.setTitle(r.getTitle());
        n.setNarrativeText(r.getNarrativeText()); n.setBranchType(r.getBranchType());
        n.setLeftOptionCode(r.getLeftOptionCode()); n.setRightOptionCode(r.getRightOptionCode());
        repo.save(n); return NodeResponse.from(n);
    }
    @Transactional public void delete(String k) {
        StoryNode n = repo.findByNodeKey(k).orElseThrow(() -> new NotFoundException("Nodo no encontrado: " + k));
        if (playthroughRepo.existsByCurrentNodeId(n.getId()))
            throw new ConflictException("No se puede borrar: hay partidas asociadas");
        repo.delete(n);
    }
    private void validate(NodeRequest r) {
        if (r.getBranchType() == BranchType.BRANCH) {
            if (isBlank(r.getLeftOptionCode()) || isBlank(r.getRightOptionCode()))
                throw new BadRequestException("Un BRANCH requiere leftOptionCode y rightOptionCode");
        }
    }
    private boolean isBlank(String s) { return s == null || s.isBlank(); }
}
