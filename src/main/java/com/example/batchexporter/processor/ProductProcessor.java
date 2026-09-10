package com.example.batchexporter.processor;

import com.example.batchexporter.dto.ProductDto;
import com.example.batchexporter.entity.Product;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ProductProcessor implements ItemProcessor<Product, ProductDto> {

    @Override
    public ProductDto process(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName().toUpperCase())
                .category(product.getCategory())
                .unitPrice(product.getPrice())
                .stockQuantity(product.getQuantity())
                .totalValue(product.getPrice().multiply(java.math.BigDecimal.valueOf(product.getQuantity())))
                .build();
    }
}
