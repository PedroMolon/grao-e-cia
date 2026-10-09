package com.pedromolon.catalog_service.domain;

import com.pedromolon.catalog_service.exception.BusinessException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testAdjustStockQuantitySuccess() {
        Product product = new Product();
        product.setType(ProductType.COFFEE_BAG);
        product.setStockQuantity(10);

        product.adjustStockQuantity(25);

        assertEquals(25, product.getStockQuantity());
    }

    @Test
    void testAdjustStockQuantityNonCoffeeBagThrowsException() {
        Product product = new Product();
        product.setType(ProductType.ESPRESSO);

        BusinessException exception = assertThrows(BusinessException.class, () -> product.adjustStockQuantity(10));
        assertEquals("Stock quantity can only be adjusted for coffee bag", exception.getMessage());
    }

    @Test
    void testAdjustStockQuantityNegativeThrowsException() {
        Product product = new Product();
        product.setType(ProductType.COFFEE_BAG);

        BusinessException exception = assertThrows(BusinessException.class, () -> product.adjustStockQuantity(-5));
        assertEquals("Quantity cannot be negative", exception.getMessage());
    }

    @Test
    void testReserveStockSuccess() {
        Product product = new Product();
        product.setType(ProductType.COFFEE_BAG);
        product.setStockQuantity(10);

        product.reserveStock(4);

        assertEquals(6, product.getStockQuantity());
    }

    @Test
    void testReserveStockInsufficientStockThrowsException() {
        Product product = new Product();
        product.setType(ProductType.COFFEE_BAG);
        product.setStockQuantity(5);

        BusinessException exception = assertThrows(BusinessException.class, () -> product.reserveStock(10));
        assertEquals("Insufficient stock", exception.getMessage());
    }

    @Test
    void testReserveStockZeroOrNegativeThrowsException() {
        Product product = new Product();
        product.setType(ProductType.COFFEE_BAG);
        product.setStockQuantity(10);

        BusinessException exception = assertThrows(BusinessException.class, () -> product.reserveStock(0));
        assertEquals("Quantity to reserve must be greater than 0", exception.getMessage());
    }

    @Test
    void testReserveStockNonCoffeeBagThrowsException() {
        Product product = new Product();
        product.setType(ProductType.SNACK);

        BusinessException exception = assertThrows(BusinessException.class, () -> product.reserveStock(2));
        assertEquals("Stock quantity can only be adjusted for coffee bag", exception.getMessage());
    }
}
