package com.pedromolon.catalog_service.dto.response;

import com.pedromolon.catalog_service.domain.ProductType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductResponse(
        Long id,
        ProductType type,
        String name,
        String description,
        BigDecimal price,
        Boolean active
) {
}
