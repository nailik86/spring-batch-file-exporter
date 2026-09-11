package com.example.batchexporter.service;

import com.example.batchexporter.dto.ProductDto;
import com.example.batchexporter.entity.Product;
import com.example.batchexporter.processor.ProductProcessor;
import com.example.batchexporter.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductExportService implements ExportService {

    private final ProductRepository productRepository;
    private final ProductProcessor productProcessor;

    public ProductExportService(ProductRepository productRepository, ProductProcessor productProcessor) {
        this.productRepository = productRepository;
        this.productProcessor = productProcessor;
    }

    @Override
    public String getName() {
        return "product";
    }

    @Override
    public List<?> fetchData() {
        return productRepository.findAllActiveProducts().stream()
                .map(productProcessor::process)
                .toList();
    }

    @Override
    public Object wrapForExport(List<?> items) {
        return items;
    }
}
