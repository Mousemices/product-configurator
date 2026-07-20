package com.festo.product_configurator.repository;

import com.festo.product_configurator.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository
        extends JpaRepository<Product, Long>,
                JpaSpecificationExecutor<Product> {

    Page<Product> findByCategoryIgnoreCase(
            String category,
            Pageable pageable
    );

    Page<Product> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );

    Page<Product> findByPriceLessThanEqual(
            double price,
            Pageable pageable
    );
}
