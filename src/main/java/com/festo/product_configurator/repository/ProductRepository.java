package com.festo.product_configurator.repository;

import com.festo.product_configurator.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {

    private final List<Product> products = new ArrayList<>();
    private Long nextId = 3L;

    public ProductRepository() {
        products.add(
                new Product(
                        1L,
                        "Pneumatic Cylinder",
                        "Actuator",
                        125.50
                )
        );

        products.add(
                new Product(
                        2L,
                        "Pressure Sensor",
                        "Sensor",
                        89.90
                ));
    }

    public List<Product> findAll() {
        return products;
    }

    public Optional<Product> findById(Long id) {
        return products.stream()
                .filter(product ->
                        product.getId().equals(id))
                .findFirst();
    }

    public Product save(Product product) {
        Product savedProduct =
                new Product(
                    nextId++,
                    product.getName(),
                    product.getCategory(),
                    product.getPrice()
                );

        products.add(savedProduct);

        return savedProduct;
    }

    public boolean deleteById(Long id) {
        return products.removeIf(
                product -> product.getId().equals(id)
        );
    }

    public Optional<Product> updateById(
            Long id,
            Product product
    ) {
        for (int i = 0; i < products.size(); i++) {
            Product existingProduct = products.get(i);

            if (existingProduct.getId().equals(id)) {
                Product updatedProduct = new Product(
                        id,
                        product.getName(),
                        product.getCategory(),
                        product.getPrice()
                );

                products.set(i, updatedProduct);

                return Optional.of(updatedProduct);
            }
        }

        return Optional.empty();
    }
}
