package com.watch.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record WatchResponse(
        UUID id,
        String brand,
        String model,
        String reference,
        String movementType,
        String caseMaterial,
        String crystalType,
        Integer waterResistanceM,
        Integer diameterMm,
        Integer lugToLugMm,
        Integer thicknessMm,
        Integer lugWidthMm,
        Long priceInCents,
        String waterResistanceLabel,
        Integer collectorScore,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
