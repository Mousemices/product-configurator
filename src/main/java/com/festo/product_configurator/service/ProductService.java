package com.festo.product_configurator.service;

import com.festo.product_configurator.dto.CreateProductRequest;
import com.festo.product_configurator.exception.ProductNotFoundException;
import com.festo.product_configurator.model.Product;
import com.festo.product_configurator.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(
            ProductRepository productRepository
    ) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id)  {
        return productRepository
                .findById(id)
                .orElseThrow(
                        () -> new ProductNotFoundException(id)
                );
    }

    public Product createProduct(CreateProductRequest request) {
        Product product =
                new Product(
                        null,
                        request.name(),
                        request.category(),
                        request.price()
                );

        return productRepository.save(product);
    }

    public boolean deleteProduct(Long id) {
        return productRepository.deleteById(id);
    }

    public Optional<Product> updateProduct(
            Long id,
            Product product
    ) {
        return productRepository.updateById(id, product);
    }
}
