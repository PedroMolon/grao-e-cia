package com.pedromolon.catalog_service.controller;

import com.pedromolon.catalog_service.domain.ProductType;
import com.pedromolon.catalog_service.dto.request.ProductRequest;
import com.pedromolon.catalog_service.dto.request.ProductStockQuantityRequest;
import com.pedromolon.catalog_service.dto.response.ProductResponse;
import com.pedromolon.catalog_service.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> save(@RequestBody @Valid ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> findAllProducts(
            @RequestParam(required = false) ProductType type,
            @RequestParam(required = false) String name,
            Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findAllProductActive(type, name, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @RequestBody @Valid ProductRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        productService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateStockQuantity(@PathVariable Long id, @RequestBody @Valid ProductStockQuantityRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateStock(id, request));
    }

    @PostMapping("/{id}/reserve")
    public ResponseEntity<ProductResponse> reserveStock(@PathVariable Long id, @RequestBody @Valid ProductStockQuantityRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.reserveStock(id, request));
    }

}
