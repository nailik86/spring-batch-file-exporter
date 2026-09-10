package com.example.batchexporter.processor;

import com.example.batchexporter.dto.ProductDto;
import com.example.batchexporter.entity.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ProductProcessorTest {

    private final ProductProcessor processor = new ProductProcessor();

    @Test
    void shouldTransformProductToDto() {
        Product product = Product.builder()
                .id(1L)
                .name("MacBook Pro")
                .category("Electronics")
                .price(new BigDecimal("2499.99"))
                .quantity(50)
                .active(true)
                .createdAt(LocalDateTime.of(2024, 1, 15, 10, 0))
                .build();

        ProductDto dto = processor.process(product);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("MACBOOK PRO");
        assertThat(dto.getCategory()).isEqualTo("Electronics");
        assertThat(dto.getUnitPrice()).isEqualByComparingTo("2499.99");
        assertThat(dto.getStockQuantity()).isEqualTo(50);
        assertThat(dto.getTotalValue()).isEqualByComparingTo("124999.50");
    }
}
