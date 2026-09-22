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
public class PlaythroughService {
    private final PlaythroughRepository repo;
    private final UserRepository userRepo;
    private final StoryNodeRepository nodeRepo;
    @Transactional public PlaythroughResponse create(PlaythroughRequest r, String authUser, boolean isAdmin) {
        User u = userRepo.findById(r.getUserId()).orElseThrow(() -> new NotFoundException("Usuario no encontrado: " + r.getUserId()));
        if (!isAdmin && !u.getUsername().equals(authUser)) throw new ForbiddenException("No puedes crear partidas para otro usuario");
        StoryNode start;
        if (r.getStartNodeKey() != null && !r.getStartNodeKey().isBlank()) {
            start = nodeRepo.findByNodeKey(r.getStartNodeKey()).orElseThrow(() -> new NotFoundException("Nodo no encontrado: " + r.getStartNodeKey()));
        } else {
            start = nodeRepo.findAll().stream().filter(n -> n.getBranchType() == BranchType.BRANCH)
                    .findFirst().orElseThrow(() -> new BadRequestException("No hay nodo BRANCH configurado como inicio"));
        }
        Playthrough p = Playthrough.builder().user(u).currentNode(start)
                .status(PlaythroughStatus.IN_PROGRESS).score(0).totalDecisions(0).build();
        repo.save(p); return PlaythroughResponse.from(p);
    }
    @Transactional(readOnly = true)
    public List<PlaythroughResponse> list(String authUser, boolean isAdmin) {
        if (isAdmin) return repo.findAll().stream().map(PlaythroughResponse::from).toList();
        User me = userRepo.findByUsername(authUser).orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        return repo.findByUserIdOrderByStartedAtDesc(me.getId()).stream().map(PlaythroughResponse::from).toList();
    }
    @Transactional(readOnly = true)
    public PlaythroughResponse get(Long id, String authUser, boolean isAdmin) {
        Playthrough p = repo.findById(id).orElseThrow(() -> new NotFoundException("Partida no encontrada: " + id));
        if (!isAdmin && !p.getUser().getUsername().equals(authUser)) throw new ForbiddenException("Esta partida no es tuya");
        return PlaythroughResponse.from(p);
    }
}
