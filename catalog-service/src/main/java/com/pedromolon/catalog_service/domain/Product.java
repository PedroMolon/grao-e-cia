package com.pedromolon.catalog_service.domain;

import com.pedromolon.catalog_service.exception.BusinessException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType type;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity = 0;

    public void adjustStockQuantity(int quantity) {
        if (this.type != ProductType.COFFEE_BAG) {
            throw new BusinessException("Stock quantity can only be adjusted for coffee bag");
        }
        if (quantity < 0) {
            throw new BusinessException("Quantity cannot be negative");
        }
        if (quantity > this.stockQuantity) {
            throw new BusinessException("Insufficient stock");
        }

        this.stockQuantity = quantity;
    }

    public void reserveStock(int quantity) {
        if (this.type != ProductType.COFFEE_BAG) {
            throw new BusinessException("Stock quantity can only be adjusted for coffee bag");
        }
        if (quantity < 0) {
            throw new BusinessException("Quantity cannot be negative");
        }
        if (quantity > this.stockQuantity) {
            throw new BusinessException("Insufficient stock");
        }

        this.stockQuantity -= quantity;
    }

}