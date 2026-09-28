package com.pedromolon.catalog_service.service;

import com.pedromolon.catalog_service.domain.Product;
import com.pedromolon.catalog_service.domain.ProductType;
import com.pedromolon.catalog_service.dto.request.ProductRequest;
import com.pedromolon.catalog_service.dto.request.ProductStockQuantityRequest;
import com.pedromolon.catalog_service.dto.response.ProductResponse;
import com.pedromolon.catalog_service.exception.ResourceNotFoundException;
import com.pedromolon.catalog_service.mapper.ProductMapper;
import com.pedromolon.catalog_service.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public ProductResponse save(ProductRequest request) {
        Product product = productMapper.toEntity(request);

        return productMapper.toResponse(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAllProductActive(ProductType type, String name, Pageable pageable) {
        if (type == null && (name == null || name.isBlank())) {
            return productRepository.findAllByActiveTrue(pageable)
                    .map(productMapper::toResponse);
        }

        String searchName = (name != null && !name.isBlank()) ? name.trim() : null;

        return productRepository.findAllActiveFiltered(type, searchName, pageable)
                .map(productMapper::toResponse);
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        productMapper.updateEntityFromRequest(request, product);

        return productMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    public void deactivate(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setActive(false);

        productRepository.save(product);
    }

    @Transactional
    public ProductResponse updateStock(Long id, ProductStockQuantityRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.adjustStockQuantity(request.quantity());

        return productMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse reserveStock(Long id, ProductStockQuantityRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.reserveStock(request.quantity());

        return productMapper.toResponse(productRepository.save(product));
    }

}
