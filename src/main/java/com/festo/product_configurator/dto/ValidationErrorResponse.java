package com.festo.product_configurator.dto;

import java.util.Map;

public record ValidationErrorResponse (

    int status,
    String error,
    String message,
    Map<String, String> fieldErrors

) {

}
