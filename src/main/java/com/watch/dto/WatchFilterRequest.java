package com.watch.dto;

public record WatchFilterRequest(
        String search,
        String brand,
        String movementType,
        String caseMaterial,
        String crystalType,
        Integer waterResistanceMin,
        Integer waterResistanceMax,
        Long priceMin,
        Long priceMax,
        Integer diameterMin,
        Integer diameterMax
) {
}
