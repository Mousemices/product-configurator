package com.festo.product_configurator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UpdateProductRequest(

        @NotBlank(
                message = "Name is required"
        )
        String name,

        @NotBlank(
                message = "Category is required"
        )
        String category,

        @Positive(
                message = "Price is required and must be positive"
        )
        double price
) {
}
