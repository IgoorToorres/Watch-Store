package com.watch.controller;

import com.watch.dto.WatchFilterRequest;
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
            @RequestParam(defaultValue = "12") Integer perPage,
            @RequestParam(defaultValue = "newest") String sort,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String movementType,
            @RequestParam(required = false) String caseMaterial,
            @RequestParam(required = false) String crystalType,
            @RequestParam(required = false) Integer waterResistanceMin,
            @RequestParam(required = false) Integer waterResistanceMax,
            @RequestParam(required = false) Long priceMin,
            @RequestParam(required = false) Long priceMax,
            @RequestParam(required = false) Integer diameterMin,
            @RequestParam(required = false) Integer diameterMax
    ){
        WatchFilterRequest filters = new WatchFilterRequest(
                search,
                brand,
                movementType,
                caseMaterial,
                crystalType,
                waterResistanceMin,
                waterResistanceMax,
                priceMin,
                priceMax,
                diameterMin,
                diameterMax
        );

        WatchPageResponse response = watchService.list(page, perPage, sort, filters);
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
