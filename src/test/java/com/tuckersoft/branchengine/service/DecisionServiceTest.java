package com.tuckersoft.branchengine.service;

import com.tuckersoft.branchengine.dto.DecisionRequest;
import com.tuckersoft.branchengine.exception.BadRequestException;
import com.tuckersoft.branchengine.exception.ConflictException;
import com.tuckersoft.branchengine.model.*;
import com.tuckersoft.branchengine.repository.DecisionRepository;
import com.tuckersoft.branchengine.repository.PlaythroughRepository;
import com.tuckersoft.branchengine.repository.StoryNodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DecisionServiceTest {

    @Mock DecisionRepository decisionRepo;
    @Mock PlaythroughRepository playthroughRepo;
    @Mock StoryNodeRepository nodeRepo;
    @Mock ApplicationEventPublisher publisher;

    DecisionService service;

    @BeforeEach
    void setUp() {
        service = new DecisionService(decisionRepo, playthroughRepo, nodeRepo, publisher);
    }

    @Test
    void stay_esBranchYSecurity() {
        assertEquals(BranchType.BRANCH,   DecisionService.classifyBranch("stay"));
        assertEquals(ImpactType.SECURITY, DecisionService.classifyImpact("stay"));
        assertEquals("SECURITY_KEY",      DecisionService.impactLabel(ImpactType.SECURITY));
    }

    @Test
    void textoLibre_esFreeformYStory() {
        assertEquals(BranchType.FREEFORM, DecisionService.classifyBranch("cualquier cosa"));
        assertEquals(ImpactType.STORY,    DecisionService.classifyImpact("cualquier cosa"));
    }

    @Test
    void destroyTheCar() {
        assertEquals(BranchType.FREEFORM, DecisionService.classifyBranch("Destroy the car"));
        assertEquals(ImpactType.SECURITY, DecisionService.classifyImpact("destroy the car"));
    }

    @Test
    void hit() {
        assertEquals(BranchType.REFUSAL, DecisionService.classifyBranch("hit"));
        assertEquals(ImpactType.QA,      DecisionService.classifyImpact("hit"));
    }

    @Test
    void partidaFinalizada_lanzaConflict() {
        User u = User.builder().id(1L).username("alice").build();
        StoryNode n = StoryNode.builder().id(1L).nodeKey("start").branchType(BranchType.BRANCH).build();
        Playthrough p = Playthrough.builder().id(10L).user(u).currentNode(n)
                .status(PlaythroughStatus.FINISHED).score(0).totalDecisions(0).build();
        when(playthroughRepo.findById(10L)).thenReturn(Optional.of(p));

        DecisionRequest req = new DecisionRequest();
        req.setInputText("stay");
        req.setChosenOptionCode("LEFT");

        assertThrows(ConflictException.class,
                () -> service.decide(10L, req, "alice", false, null));
    }

    // 6) BRANCH sin leftOptionCode → BadRequest antes de tocar el repo
    @Test
    void sinCaminoDefinido_lanzaBadRequest() {
        User u = User.builder().id(1L).username("alice").build();
        StoryNode n = StoryNode.builder().id(1L).nodeKey("start")
                .branchType(BranchType.BRANCH).leftOptionCode(null).build();
        Playthrough p = Playthrough.builder().id(10L).user(u).currentNode(n)
                .status(PlaythroughStatus.IN_PROGRESS).score(0).totalDecisions(0).build();
        when(playthroughRepo.findById(10L)).thenReturn(Optional.of(p));
        // NO stub de nodeRepo: no se llega a llamar

        DecisionRequest req = new DecisionRequest();
        req.setInputText("stay");
        req.setChosenOptionCode("LEFT");

        assertThrows(BadRequestException.class,
                () -> service.decide(10L, req, "alice", false, null));
    }
}
