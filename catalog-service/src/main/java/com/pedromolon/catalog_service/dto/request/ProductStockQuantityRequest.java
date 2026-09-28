package com.pedromolon.catalog_service.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductStockQuantityRequest(
        @NotNull(message = "Quantity cannot be null")
        @Positive(message = "Quantity must be positive")
        int quantity
) {
}
