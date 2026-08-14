package com.watch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record WatchRequest(

        @NotBlank(message = "Brand is required and cannot be blank.")
        String brand,

        @NotBlank(message = "Model is required and cannot be blank.")
        String model,

        @NotBlank(message = "Reference is required and cannot be blank.")
        String reference,

        @NotBlank(message = "Movement type is required.")
        String movementType,

        @NotBlank(message = "Case material is required.")
        String caseMaterial,

        @NotBlank(message = "Crystal type is required.")
        String crystalType,

        @NotNull(message = "Water resistance is required.")
        @PositiveOrZero(message = "Water resistance must be greater than or equal to zero.")
        Integer waterResistanceM,

        @NotNull(message = "Diameter is required.")
        @Positive(message = "Diameter must be greater than zero.")
        Integer diameterMm,

        @NotNull(message = "Lug-to-lug measurement is required.")
        @Positive(message = "Lug-to-lug measurement must be greater than zero.")
        Integer lugToLugMm,

        @NotNull(message = "Thickness is required.")
        @Positive(message = "Thickness must be greater than zero.")
        Integer thicknessMm,

        @NotNull(message = "Lug width is required.")
        @Positive(message = "Lug width must be greater than zero.")
        Integer lugWidthMm,

        @NotNull(message = "Price is required.")
        @PositiveOrZero(message = "Price must be greater than or equal to zero.")
        Long priceInCents
) {
}
