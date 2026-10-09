package com.pedromolon.catalog_service.service;

import com.pedromolon.catalog_service.domain.Product;
import com.pedromolon.catalog_service.domain.ProductType;
import com.pedromolon.catalog_service.dto.request.ProductRequest;
import com.pedromolon.catalog_service.dto.request.ProductStockQuantityRequest;
import com.pedromolon.catalog_service.dto.response.ProductResponse;
import com.pedromolon.catalog_service.exception.ResourceNotFoundException;
import com.pedromolon.catalog_service.mapper.ProductMapper;
import com.pedromolon.catalog_service.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductRequest productRequest;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        product = new Product(1L, ProductType.COFFEE_BAG, "Café Especial", "Descrição", new BigDecimal("35.00"), true, 10);
        productRequest = new ProductRequest(ProductType.COFFEE_BAG, "Café Especial", "Descrição", new BigDecimal("35.00"));
        productResponse = ProductResponse.builder()
                .id(1L)
                .type(ProductType.COFFEE_BAG)
                .name("Café Especial")
                .description("Descrição")
                .price(new BigDecimal("35.00"))
                .active(true)
                .stockQuantity(10)
                .build();
    }

    @Test
    void testSaveProductSuccess() {
        when(productMapper.toEntity(any(ProductRequest.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        ProductResponse response = productService.save(productRequest);

        assertNotNull(response);
        assertEquals("Café Especial", response.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testFindByIdSuccess() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        ProductResponse response = productService.findById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void testFindByIdNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.findById(99L));
    }

    @Test
    void testFindAllProductActiveWithoutFilter() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(product));

        when(productRepository.findAllByActiveTrue(pageable)).thenReturn(page);
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        Page<ProductResponse> result = productService.findAllProductActive(null, null, pageable);

        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findAllByActiveTrue(pageable);
    }

    @Test
    void testUpdateStockSuccess() {
        ProductStockQuantityRequest request = new ProductStockQuantityRequest(20);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        ProductResponse response = productService.updateStock(1L, request);

        assertNotNull(response);
        assertEquals(20, product.getStockQuantity());
    }

    @Test
    void testDeactivateSuccess() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deactivate(1L);

        assertFalse(product.getActive());
        verify(productRepository, times(1)).save(product);
    }
}
