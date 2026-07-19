package com.festo.product_configurator.controller;

import com.festo.product_configurator.dto.CreateProductRequest;
import com.festo.product_configurator.model.Product;
import com.festo.product_configurator.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(
            ProductService productService
    ) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
            @PathVariable Long id
    ) {
        Product product = productService.getProductById(id);

        return ResponseEntity.ok(product); // productService.getProductById(id);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(
            //@Valid
            @RequestBody CreateProductRequest request
    ) {
        Product createdProduct = productService.createProduct(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id
    ) {
        boolean deleted = productService.deleteProduct(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product
    ) {
        return productService
                .updateProduct(id, product)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                    ResponseEntity
                            .notFound()
                            .build()
                );
    }
}
