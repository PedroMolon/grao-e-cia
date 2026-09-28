package com.pedromolon.catalog_service.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductStockQuantityRequest(
        @NotNull(message = "Quantity cannot be null")
        @PositiveOrZero(message = "Quantity must be positive or zero")
        int quantity
) {
}
