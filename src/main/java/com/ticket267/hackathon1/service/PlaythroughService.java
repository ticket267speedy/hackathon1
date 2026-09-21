package com.ticket267.hackathon1.service;

import com.ticket267.hackathon1.dto.PlaythroughRequest;
import com.ticket267.hackathon1.dto.PlaythroughResponse;
import com.ticket267.hackathon1.exception.BadRequestException;
import com.ticket267.hackathon1.exception.ForbiddenException;
import com.ticket267.hackathon1.exception.NotFoundException;
import com.ticket267.hackathon1.model.*;
import com.ticket267.hackathon1.repository.PlaythroughRepository;
import com.ticket267.hackathon1.repository.PlayerRepository;
import com.ticket267.hackathon1.repository.WorkflowNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaythroughService {

    private final PlaythroughRepository repo;
    private final PlayerRepository playerRepo;
    private final WorkflowNodeRepository nodeRepo;

    @Transactional
    public PlaythroughResponse create(PlaythroughRequest req, String authUsername, boolean isAdmin) {
        Player player = playerRepo.findById(req.getUserId())
                .orElseThrow(() -> new NotFoundException("Player no encontrado: " + req.getUserId()));

        if (!isAdmin && !player.getUsername().equals(authUsername))
            throw new ForbiddenException("No puedes crear partidas para otro usuario");

        WorkflowNode start;
        if (req.getStartNodeKey() != null && !req.getStartNodeKey().isBlank()) {
            start = nodeRepo.findByNodeKey(req.getStartNodeKey())
                    .orElseThrow(() -> new NotFoundException("Nodo no encontrado: " + req.getStartNodeKey()));
            if (start.getNodeType() != NodeType.START)
                throw new BadRequestException("El nodo inicial debe ser de tipo START");
        } else {
            start = nodeRepo.findFirstByIsStartTrue()
                    .orElseThrow(() -> new BadRequestException("No hay nodo START configurado"));
        }

        Playthrough p = Playthrough.builder()
                .player(player)
                .currentNode(start)
                .status(PlaythroughStatus.IN_PROGRESS)
                .totalDecisions(0)
                .build();
        repo.save(p);
        return PlaythroughResponse.from(p);
    }

    public List<PlaythroughResponse> list(String authUsername, boolean isAdmin) {
        if (isAdmin) {
            return repo.findAll().stream().map(PlaythroughResponse::from).toList();
        }
        Player me = playerRepo.findByUsername(authUsername)
                .orElseThrow(() -> new NotFoundException("Player no encontrado"));
        return repo.findByPlayerIdOrderByStartedAtDesc(me.getId()).stream()
                .map(PlaythroughResponse::from).toList();
    }

    public PlaythroughResponse get(Long id, String authUsername, boolean isAdmin) {
        Playthrough p = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Partida no encontrada: " + id));
        if (!isAdmin && !p.getPlayer().getUsername().equals(authUsername))
            throw new ForbiddenException("Esta partida no es tuya");
        return PlaythroughResponse.from(p);
    }
}
