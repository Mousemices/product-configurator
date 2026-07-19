package com.festo.product_configurator.dto;

public record ApiError(
        int status,
        String error,
        String message
) {
}
