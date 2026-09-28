package com.pedromolon.catalog_service.repository;

import com.pedromolon.catalog_service.domain.Product;
import com.pedromolon.catalog_service.domain.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAllByActiveTrue(Pageable pageable);

    @Query("""
        SELECT p FROM Product p
        WHERE p.active = true
          AND (:type IS NULL OR p.type = :type)
          AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
    """)
    Page<Product> findAllActiveFiltered(
            @Param("type") ProductType type,
            @Param("name") String name,
            Pageable pageable
    );

}
