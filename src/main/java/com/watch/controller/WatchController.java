package com.watch.controller;

import com.watch.dto.WatchPageResponse;
import com.watch.dto.WatchRequest;
import com.watch.dto.WatchResponse;
import com.watch.service.WatchService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/watches")
public class WatchController {
    private final WatchService watchService;

    public WatchController(WatchService watchService){
        this.watchService = watchService;
    }

    @PostMapping
    public ResponseEntity<WatchResponse> create(@Valid @RequestBody WatchRequest request){
        WatchResponse response = watchService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<WatchPageResponse> listAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "12") Integer perPage
    ){
        WatchPageResponse response = watchService.list(page, perPage);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WatchResponse> findById(@PathVariable UUID id){
        WatchResponse response = watchService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WatchResponse> update(@Valid @RequestBody WatchRequest request, @PathVariable UUID id){
        WatchResponse response = watchService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        watchService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
