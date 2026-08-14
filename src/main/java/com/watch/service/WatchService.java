package com.watch.service;

import com.watch.dto.WatchRequest;
import com.watch.dto.WatchResponse;
import com.watch.entity.CaseMaterial;
import com.watch.entity.CrystalType;
import com.watch.entity.MovementType;
import com.watch.entity.Watch;
import com.watch.exception.ResourceNotFoundException;
import com.watch.mapper.WatchMapper;
import com.watch.repository.WatchRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class WatchService {

    private final WatchMapper watchMapper;
    private final WatchRepository watchRepository;

    public WatchService(
            WatchMapper watchMapper,
            WatchRepository watchRepository
    ){
        this.watchMapper = watchMapper;
        this.watchRepository = watchRepository;
    }

    public WatchResponse create(WatchRequest request){
        Watch watch = watchMapper.toEntity(request);
        Watch savedWatch = watchRepository.save(watch);
        return watchMapper.toResponse(savedWatch);
    }

    public WatchResponse findById(UUID id){
        Watch watch = watchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Relogio não encontrado"));
        return watchMapper.toResponse(watch);
    }

    public void delete(UUID id){
        watchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Relogio não encontrado"));
        watchRepository.deleteById(id);
    }

    public List<WatchResponse> list(){
        List<Watch> watches = watchRepository.findAll();
        List<WatchResponse> watchesResponse = new ArrayList<>();
        for(Watch watch : watches){
            WatchResponse response = watchMapper.toResponse(watch);
            watchesResponse.add(response);
        }
        return watchesResponse;
    }

    public WatchResponse update(UUID id, WatchRequest request){
        Watch watch = watchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Relogio não encontrado"));

        watch.update(
                request.brand(),
                request.model(),
                request.reference(),
                MovementType.fromApi(request.movementType()),
                CaseMaterial.fromApi(request.caseMaterial()),
                CrystalType.fromApi(request.crystalType()),
                request.waterResistanceM(),
                request.diameterMm(),
                request.lugToLugMm(),
                request.thicknessMm(),
                request.lugWidthMm(),
                request.priceInCents()
        );

        Watch savedWatch = watchRepository.save(watch);
        return watchMapper.toResponse(savedWatch);
    }
}
