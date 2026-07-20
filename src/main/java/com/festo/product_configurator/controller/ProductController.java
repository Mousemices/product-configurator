package com.festo.product_configurator.controller;

import com.festo.product_configurator.dto.CreateProductRequest;
import com.festo.product_configurator.dto.UpdateProductRequest;
import com.festo.product_configurator.model.Product;
import com.festo.product_configurator.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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
    public Page<Product> getProducts(
            @RequestParam(name = "category", required = false)
            String category,

                @RequestParam(name = "search", required = false)
            String search,

            @RequestParam(name= "maxPrice", required = false)
            Double maxPrice,

            @RequestParam(name = "page", defaultValue = "0")
            int page,

            @RequestParam(name = "size", defaultValue = "20")
            int size,

            @RequestParam(name = "sortBy", defaultValue = "id")
            String sortBy,

            @RequestParam(name = "direction", defaultValue = "asc")
            String direction
    ) {
        return productService.getProducts(
                category,
                search,
                maxPrice,
                page,
                size,
                sortBy,
                direction
        );
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
            @Valid
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
        productService.deleteProduct(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request
            ) {

        Product updatedProduct = productService.updateProduct(id, request);

        return ResponseEntity.ok(updatedProduct);
    }
}
