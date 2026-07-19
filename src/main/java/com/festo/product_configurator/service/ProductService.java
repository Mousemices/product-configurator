package com.festo.product_configurator.service;

import com.festo.product_configurator.dto.CreateProductRequest;
import com.festo.product_configurator.dto.UpdateProductRequest;
import com.festo.product_configurator.exception.InvalidProductFilterException;
import com.festo.product_configurator.exception.ProductNotFoundException;
import com.festo.product_configurator.model.Product;
import com.festo.product_configurator.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<Product> getProducts(
            String category,
            String search,
            Double maxPrice
    ) {
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
            return productRepository.findByCategoryIgnoreCase(category);
        }

        if (search != null && !search.isBlank()) {
            return productRepository.findByNameContainingIgnoreCase(search);
        }

        if (maxPrice != null) {
            if (maxPrice < 0) {
                throw new InvalidProductFilterException(
                        "maxPrice must be greater than 0"
                );
            }

            return productRepository.findByPriceLessThanEqual(maxPrice);
        }

        return productRepository.findAll();
    }
}
