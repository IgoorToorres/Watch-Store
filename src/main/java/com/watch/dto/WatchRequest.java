package com.watch.dto;

public record WatchRequest(
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
      Long priceInCents
) {
}
