package com.ticket267.hackathon1.controller;

import com.ticket267.hackathon1.dto.NodeRequest;
import com.ticket267.hackathon1.dto.NodeResponse;
import com.ticket267.hackathon1.service.NodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nodes")
@RequiredArgsConstructor
public class NodeController {

    private final NodeService service;

    @GetMapping
    public ResponseEntity<List<NodeResponse>> list() {
        return ResponseEntity.ok(service.listAll());
    }

    @GetMapping("/{nodeKey}")
    public ResponseEntity<NodeResponse> get(@PathVariable String nodeKey) {
        return ResponseEntity.ok(service.getByKey(nodeKey));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NodeResponse> create(@Valid @RequestBody NodeRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @PutMapping("/{nodeKey}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NodeResponse> update(@PathVariable String nodeKey,
                                               @Valid @RequestBody NodeRequest req) {
        return ResponseEntity.ok(service.update(nodeKey, req));
    }

    @DeleteMapping("/{nodeKey}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String nodeKey) {
        service.delete(nodeKey);
        return ResponseEntity.noContent().build();
    }
}
