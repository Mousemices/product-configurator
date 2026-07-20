package com.festo.product_configurator.dto;

public record ProductResponse(
        Long id,

        String name,

        String category,

        double price
) {
}
