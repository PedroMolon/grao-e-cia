package com.pedromolon.catalog_service.service;

import com.pedromolon.catalog_service.domain.Product;
import com.pedromolon.catalog_service.dto.request.ProductRequest;
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
    public Page<ProductResponse> findAllProductActive(Pageable pageable) {
        return productRepository.findAllByActiveTrue(pageable)
                .map(productMapper::toResponse);
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setType(request.type());
        product.setPrice(request.price());

        return productMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    public void desactive(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setActive(false);

        productRepository.save(product);
    }

}
