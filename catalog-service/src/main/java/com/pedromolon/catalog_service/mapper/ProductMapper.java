package com.pedromolon.catalog_service.mapper;

import com.pedromolon.catalog_service.domain.Product;
import com.pedromolon.catalog_service.dto.request.ProductRequest;
import com.pedromolon.catalog_service.dto.response.ProductResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductRequest productRequest);

    ProductResponse toResponse(Product product);

}
