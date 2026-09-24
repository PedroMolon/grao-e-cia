package com.pedromolon.catalog_service.dto.request;

import com.pedromolon.catalog_service.domain.ProductType;

import java.math.BigDecimal;

public record ProductRequest(
        ProductType type,
        String name,
        String description,
        BigDecimal price
) {
}
