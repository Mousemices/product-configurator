package com.festo.product_configurator.service;

import com.festo.product_configurator.dto.CreateProductRequest;
import com.festo.product_configurator.dto.UpdateProductRequest;
import com.festo.product_configurator.exception.InvalidProductFilterException;
import com.festo.product_configurator.exception.ProductNotFoundException;
import com.festo.product_configurator.model.Product;
import com.festo.product_configurator.repository.ProductRepository;
import com.festo.product_configurator.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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

        List<Specification<Product>> specifications =
                new ArrayList<>();

        if (category != null && !category.isBlank()) {
            specifications.add(
                    ProductSpecification.hasCategoryIgnoreCase(category)
            );
        }

        if (search != null && !search.isBlank()) {
            specifications.add(
                    ProductSpecification.nameContainsIgnoreCase(search)
            );
        }

        if (maxPrice != null) {
            if (maxPrice < 0) {
                throw new InvalidProductFilterException(
                        "maxPrice must be greater than 0"
                );
            }

            specifications.add(
                    ProductSpecification.priceLessThanEqual(maxPrice)
            );
        }

        Specification<Product> specification =
                Specification.allOf(specifications);

        return productRepository.findAll(specification, pageable);
    }
}
