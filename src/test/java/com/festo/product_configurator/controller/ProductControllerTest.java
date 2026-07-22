package com.festo.product_configurator.controller;

import com.festo.product_configurator.dto.CreateProductRequest;
import com.festo.product_configurator.dto.ProductResponse;
import com.festo.product_configurator.exception.ProductNotFoundException;
import com.festo.product_configurator.mapper.ProductMapper;
import com.festo.product_configurator.model.Product;
import com.festo.product_configurator.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private ProductMapper productMapper;

    @Test
    void shouldReturnProductWhenProductExists() throws Exception {
        // Arrange
        Product product =
                new Product(
                        "Pressure sensor",
                        "Sensor",
                        89.90
                );

        ProductResponse response =
                new ProductResponse(
                        1L,
                        "Pressure Sensor",
                        "Sensor",
                        89.90
                );

        when(
                productService.getProductById(1L)
        ).thenReturn(
                product
        );

        when(
                productMapper.toResponse(product)
        ).thenReturn(
                response
        );

        // Act
        mockMvc.perform(
                get("/api/products/1")
        ).andExpect(
                status().isOk()
        ).andExpect(
                content()
                        .contentTypeCompatibleWith(
                                MediaType.APPLICATION_JSON
                        )
        ).andExpect(
                jsonPath("$.id").value(1L)
        ).andExpect(
                jsonPath("$.name").value("Pressure Sensor")
        ).andExpect(
                jsonPath("$.category").value("Sensor")
        ).andExpect(
                jsonPath("$.price").value(89.90)
        );

        // Assert
        verify(
                productService
        ).getProductById(1L);

        verify(
                productMapper
        ).toResponse(product);
    }

    @Test
    void shouldReturn404WhenProductDoesNotExists() throws Exception {
        // Arrange
        when(
                productService.getProductById(255L)
        ).thenThrow(
                new ProductNotFoundException(255L)
        );

        // Act
        mockMvc.perform(
                get("/api/products/255")
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                jsonPath("$.status").value(404)
        ).andExpect(
                jsonPath("$.error").value("Not found")
        ).andExpect(
                jsonPath("$.message").value("Product with id 255 was not found.")
        );

        // Assert
        verify(
                productService
        ).getProductById(255L);

        verifyNoInteractions(
                productMapper
        );
    }

    @Test
    void shouldCreateProductWhenRequestIsValid() throws Exception {
        // Arrange
        Product createdProduct =
                new Product(
                        "Pressure sensor",
                        "Sensor",
                        89.90
                );

        ProductResponse response =
                new ProductResponse(
                        1L,
                        "Pressure sensor",
                        "Sensor",
                        89.90
                );

        when(
                productService.createProduct(
                        any(CreateProductRequest.class)
                )
        ).thenReturn(
                createdProduct
        );

        when(
                productMapper.toResponse(createdProduct)
        ).thenReturn(
                response
        );

        String requestBody = """
                {
                    "name": "Pressure sensor",
                    "category": "Sensor",
                    "price": 89.90
                }
                """;

        // Act
        mockMvc.perform(
                post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        ).andExpect(
                status().isCreated()
        ).andExpect(
                content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        ).andExpect(
                jsonPath("$.id").value(1)
        ).andExpect(
                jsonPath("$.name").value("Pressure sensor")
        ).andExpect(
                jsonPath("$.category").value("Sensor")
        ).andExpect(
                jsonPath("$.price").value(89.90)
        );

        // Assert
        verify(productService).createProduct(any(CreateProductRequest.class));
        verify(productMapper).toResponse(createdProduct);
    }

    @Test
    void shouldReturn400WhenCreateProductRequestIsInvalid() throws Exception {

        // Arrange
        String requestBody = """
                {
                    "name": "",
                    "category": "",
                    "price": -10
                }
                """;

        // Act
        mockMvc.perform(
                post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        ).andExpect(
                status().isBadRequest()
        ).andExpect(
                content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
        ).andExpect(
                jsonPath("$.status").value(400)
        ).andExpect(
                jsonPath("$.error").value("Bad request")
        ).andExpect(
                jsonPath("$.message").value("Validation failed")
        ).andExpect(
                jsonPath("$.fieldErrors.name").value("Name is required")
        ).andExpect(
                jsonPath("$.fieldErrors.category").value("Category is required")
        ).andExpect(
                jsonPath("$.fieldErrors.price").value("Price is required and must be positive")
        );

        // Assert
        verifyNoInteractions(productService);
        verifyNoInteractions(productMapper);
    }

    @Test
    void shouldDeleteProductAndReturn204() throws Exception {
        mockMvc.perform(
                delete("/api/products/1")
        ).andExpect(
                status().isNoContent()
        ).andExpect(
                content().string("")
        );

        verify(
                productService
        ).deleteProduct(1L);

        verifyNoInteractions(productMapper);
    }

    @Test
    void shouldReturn404WhenDeleteProductDoesNotExists() throws Exception {

        doThrow(
                new ProductNotFoundException(255L)
        ).when(
                productService
        ).deleteProduct(255L);

        mockMvc.perform(
                delete("/api/products/255")
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                jsonPath("$.status").value(404)
        ).andExpect(
                jsonPath("$.error").value("Not found")
        ).andExpect(
                jsonPath("$.message").value("Product with id 255 was not found.")
        );

        verify(productService).deleteProduct(255L);
    }
}
