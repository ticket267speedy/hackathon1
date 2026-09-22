package com.tuckersoft.branchengine.controller;
import com.tuckersoft.branchengine.dto.*;
import com.tuckersoft.branchengine.service.NodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/v1/nodes") @RequiredArgsConstructor
public class NodeController {
    private final NodeService svc;
    @GetMapping public ResponseEntity<List<NodeResponse>> list(){return ResponseEntity.ok(svc.list());}
    @GetMapping("/{key}") public ResponseEntity<NodeResponse> get(@PathVariable String key){return ResponseEntity.ok(svc.getByKey(key));}
    @PostMapping @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NodeResponse> c(@Valid @RequestBody NodeRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(svc.create(r));}
    @PutMapping("/{key}") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NodeResponse> u(@PathVariable String key,@Valid @RequestBody NodeRequest r){return ResponseEntity.ok(svc.update(key,r));}
    @DeleteMapping("/{key}") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> d(@PathVariable String key){svc.delete(key);return ResponseEntity.noContent().build();}
}
