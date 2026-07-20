package com.festo.product_configurator.service;

import com.festo.product_configurator.dto.CreateProductRequest;
import com.festo.product_configurator.dto.UpdateProductRequest;
import com.festo.product_configurator.exception.InvalidProductFilterException;
import com.festo.product_configurator.exception.ProductNotFoundException;
import com.festo.product_configurator.model.Product;
import com.festo.product_configurator.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(
            ProductRepository productRepository
    ) {
        this.productRepository = productRepository;
    }

    /*public List<Product> getAllProducts() {
        return productRepository.findAll();
    }*/

    public Product createProduct(CreateProductRequest request) {
        Product product =
                new Product(
                        request.name(),
                        request.category(),
                        request.price()
                );

        return productRepository.save(product);
    }

    public Product getProductById(Long id)  {
        return productRepository
                .findById(id)
                .orElseThrow(
                        () -> new ProductNotFoundException(id)
                );
    }

    public void deleteProduct(Long id) {

        Product product =
                productRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new ProductNotFoundException(id)
                        );

        productRepository.delete(product);
    }

    @Transactional
    public Product updateProduct(
            Long id,
            UpdateProductRequest request
    ) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException(id)
        );

        product.update(
                request.name(),
                request.category(),
                request.price()
        );

        return product;
    }

    public Page<Product> getProducts(
            String category,
            String search,
            Double maxPrice,
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        if (page < 0 ) {
            throw new InvalidProductFilterException(
                    "Page must be greater than or equal to 0"
            );
        }

        if (size < 0 || size > 100) {
            throw new InvalidProductFilterException(
                    "Size must be between 0 and 100"
            );
        }

        Set<String> allowedSortFields =
                Set.of(
                        "id", "name", "category", "price"
                );

        if (!allowedSortFields.contains(sortBy)) {
            throw new InvalidProductFilterException(
                    "Invalid sort field " + sortBy
            );
        }

        Sort.Direction sortDirection;

        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException exception) {
            throw new InvalidProductFilterException(
                    "Direction must be asc or desc"
            );
        }

        Sort sort = Sort.by(sortDirection, sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);

        int filterCount = 0;

        if (category != null && !category.isBlank()) {
            filterCount++;
        }

        if (search != null && !search.isBlank()) {
            filterCount++;
        }

        if (maxPrice != null) {
            filterCount++;
        }

        if (filterCount > 1) {
            throw new InvalidProductFilterException(
                    "Only one filter can be used at a time"
            );
        }

        if (category != null && !category.isBlank()) {
            return productRepository
                    .findByCategoryIgnoreCase(
                            category,
                            pageable
                    );
        }

        if (search != null && !search.isBlank()) {
            return productRepository
                    .findByNameContainingIgnoreCase(
                            search,
                            pageable
                    );
        }

        if (maxPrice != null) {
            if (maxPrice < 0) {
                throw new InvalidProductFilterException(
                        "maxPrice must be greater than 0"
                );
            }

            return productRepository
                    .findByPriceLessThanEqual(
                            maxPrice,
                            pageable
                    );
        }

        return productRepository.findAll(pageable);
    }
}
