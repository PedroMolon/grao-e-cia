package com.pedromolon.catalog_service.dto.request;

import com.pedromolon.catalog_service.domain.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Product type is required") ProductType type,
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "Description is required") String description,
        @NotNull(message = "Price is required") @Positive(message = "Price must be positive") BigDecimal price
) {
}
