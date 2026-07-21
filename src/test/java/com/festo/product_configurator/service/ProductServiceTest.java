package com.festo.product_configurator.service;

import com.festo.product_configurator.dto.CreateProductRequest;
import com.festo.product_configurator.exception.ProductNotFoundException;
import com.festo.product_configurator.model.Product;
import com.festo.product_configurator.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldReturnProductWhenProductExists() {

        Product product =
                new Product(
                        "Pressure sensor",
                        "Sensor",
                        89.90
                );

        when(
                productRepository.findById(1L)
        ).thenReturn(
                Optional.of(product)
        );

        Product result = productService.getProductById(1L);

        assertSame(
                product,
                result
        );

        verify(
                productRepository
        ).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenProductDoesNotExist() {
        when(
                productRepository.findById(255L)
        ).thenReturn(
                Optional.empty()
        );

        ProductNotFoundException exception =
                assertThrows(
                        ProductNotFoundException.class,
                        () -> productService.getProductById(255L)
                );

        assertEquals("Product with id 255 was not found.", exception.getMessage());

        verify(
                productRepository
        ).findById(255L);
    }

    @Test
    void shouldCreateProduct() {
        // Arrange
        CreateProductRequest request = new CreateProductRequest(
                "Pressure sensor",
                "Sensor",
                89.90
        );

        when(
                productRepository.save(any(Product.class))
        ).thenAnswer(
                invocation -> invocation.getArgument(0)
        );

        // Act
        Product result = productService.createProduct(request);

        // Assert
        assertEquals("Pressure sensor", result.getName());
        assertEquals("Sensor", result.getCategory());
        assertEquals(89.90, result.getPrice());

        verify(productRepository).save(any(Product.class));
    }
}
