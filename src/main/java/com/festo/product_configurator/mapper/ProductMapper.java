package com.festo.product_configurator.mapper;

import com.festo.product_configurator.dto.ProductResponse;
import com.festo.product_configurator.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductResponse toResponse(
            Product product
    ) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice()
        );
    }
}
