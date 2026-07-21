package com.festo.product_configurator.service;

import com.festo.product_configurator.dto.CreateProductRequest;
import com.festo.product_configurator.dto.UpdateProductRequest;
import com.festo.product_configurator.exception.ProductNotFoundException;
import com.festo.product_configurator.model.Product;
import com.festo.product_configurator.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        productService.createProduct(request);

        // Assert
        ArgumentCaptor<Product> captor =
                ArgumentCaptor.forClass(
                        Product.class
                );

        verify(productRepository).save(captor.capture());


        Product savedProduct = captor.getValue();

        assertEquals("Pressure sensor", savedProduct.getName());
        assertEquals("Sensor", savedProduct.getCategory());
        assertEquals(89.90, savedProduct.getPrice());

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldDeleteProductWhenExists() {
        // Arrange
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

        // ACt
        productService.deleteProduct(1L);

        // Assert
        verify(productRepository).findById(1L);
        verify(productRepository).delete(product);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingProduct() {
        // Arrange
        when(
                productRepository.findById(255L)
        ).thenReturn(
                Optional.empty()
        );

        // Act
        assertThrows(
                ProductNotFoundException.class,
                () -> productService.deleteProduct(255L)
        );

        // Assert
        verify(productRepository).findById(255L);
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    void shouldUpdateProductWhenProductExists() {
        // Arrange
        Product product =
                new Product(
                        "Pressure sensor",
                        "Sensor",
                        89.90
                );

        UpdateProductRequest request =
                new UpdateProductRequest(
                        "Updated sensor",
                        "Sensor",
                        99.90
                );

        when(
                productRepository.findById(1L)
        ).thenReturn(
                Optional.of(product)
        );

        // Act
        Product result = productService.updateProduct(1L, request);

        // Assert
        assertEquals("Updated sensor", result.getName());
        assertEquals("Sensor", result.getCategory());
        assertEquals(99.90, result.getPrice());

        verify(productRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingProduct() {
        // Arrange
        when(
                productRepository.findById(255L)
        ).thenReturn(
                Optional.empty()
        );

        UpdateProductRequest request =
                new UpdateProductRequest(
                        "Updated sensor",
                        "Sensor",
                        89.90
                );

        // Act
        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productService.updateProduct(255L, request)
        );

        // Assert
        assertEquals("Product with id 255 was not found.", exception.getMessage());

        verify(productRepository).findById(255L);
        verifyNoMoreInteractions(productRepository);
    }

}
