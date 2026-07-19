package com.festo.product_configurator.exception;

import com.festo.product_configurator.dto.ApiError;
import com.festo.product_configurator.dto.ValidationErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            ProductNotFoundException.class
    )
    public ResponseEntity<ApiError> handleProductNotFoundException(
            ProductNotFoundException exception
    ) {

        ApiError apiError = new ApiError(
                404,
                "Not found",
                exception.getMessage()
        );

        return ResponseEntity
                .status(404)
                .body(apiError);
    }

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<ValidationErrorResponse>
    handleValidationException(
            MethodArgumentNotValidException exception
    ) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();

        exception
                .getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                    fieldErrors.put(
                            error.getField(),
                            error.getDefaultMessage()
                    )
                );

        ValidationErrorResponse response =
                new ValidationErrorResponse(
                        400,
                        "Bad request",
                        "Validation failed",
                        fieldErrors
                );

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(
            InvalidProductFilterException.class
    )
    public ResponseEntity<ApiError> handleInvalidProductFilterException(
            InvalidProductFilterException exception
    ) {
        ApiError apiError = new ApiError(
                400,
                "Bad request",
                exception.getMessage()
        );

        return ResponseEntity
                .badRequest()
                .body(apiError);
    }
}
