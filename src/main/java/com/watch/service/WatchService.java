package com.watch.service;

import com.watch.dto.WatchPageResponse;
import com.watch.dto.WatchFilterRequest;
import com.watch.dto.WatchRequest;
import com.watch.dto.WatchResponse;
import com.watch.entity.CaseMaterial;
import com.watch.entity.CrystalType;
import com.watch.entity.MovementType;
import com.watch.entity.Watch;
import com.watch.exception.DomainException;
import com.watch.exception.ResourceNotFoundException;
import com.watch.mapper.WatchMapper;
import com.watch.repository.WatchRepository;
import com.watch.specification.WatchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    public WatchPageResponse list(Integer page, Integer perPage, String sort, WatchFilterRequest filters){
        if (page == null || page < 1) {
            throw new DomainException("A página deve ser maior ou igual a 1.");
        }

        if (perPage == null || perPage < 1) {
            throw new DomainException("A quantidade de itens por página deve ser maior ou igual a 1.");
        }

        if (perPage > 60) {
            throw new DomainException("A quantidade de itens por página não pode ser maior que 60.");
        }

        validateFilters(filters);

        Sort sortOption = buildSort(sort);
        Pageable pageable = PageRequest.of(page - 1, perPage, sortOption);
        Specification<Watch> specification = WatchSpecification.withFilters(filters);

        Page<Watch> watchesPage = watchRepository.findAll(specification, pageable);
        List<WatchResponse> items = new ArrayList<>();
        for(Watch watch : watchesPage.getContent()){
            WatchResponse response = watchMapper.toResponse(watch);
            items.add(response);
        }
        return new WatchPageResponse(
                items,
                watchesPage.getTotalElements(),
                page,
                perPage,
                watchesPage.getTotalPages()
        );
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

    private Sort buildSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }

        String normalizedSort = sort.trim().toLowerCase();

        return switch (normalizedSort) {
            case "newest" -> Sort.by(Sort.Direction.DESC, "createdAt");
            case "price_asc" -> Sort.by(Sort.Direction.ASC, "priceInCents");
            case "price_desc" -> Sort.by(Sort.Direction.DESC, "priceInCents");
            case "diameter_asc" -> Sort.by(Sort.Direction.ASC, "diameterMm");
            case "wr_desc" -> Sort.by(Sort.Direction.DESC, "waterResistanceM");
            default -> throw new DomainException(
                    "Ordenação inválida. Valores aceitos: newest, price_asc, price_desc, diameter_asc, wr_desc."
            );
        };
    }

    private void validateFilters(WatchFilterRequest filters) {
        if (filters == null) {
            return;
        }

        validateIntegerRange(
                filters.waterResistanceMin(),
                filters.waterResistanceMax(),
                "A resistência mínima não pode ser maior que a resistência máxima."
        );

        validateLongRange(
                filters.priceMin(),
                filters.priceMax(),
                "O preço mínimo não pode ser maior que o preço máximo."
        );

        validateIntegerRange(
                filters.diameterMin(),
                filters.diameterMax(),
                "O diâmetro mínimo não pode ser maior que o diâmetro máximo."
        );
    }

    private void validateIntegerRange(Integer min, Integer max, String message) {
        if (min != null && max != null && min > max) {
            throw new DomainException(message);
        }
    }

    private void validateLongRange(Long min, Long max, String message) {
        if (min != null && max != null && min > max) {
            throw new DomainException(message);
        }
    }
}
