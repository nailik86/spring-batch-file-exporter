package com.example.batchexporter.reader;

import com.example.batchexporter.entity.Product;
import com.example.batchexporter.repository.ProductRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class ProductReader implements ItemReader<Product> {

    private final ProductRepository productRepository;
    private Iterator<Product> iterator;

    public ProductReader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product read() {
        if (iterator == null) {
            List<Product> products = productRepository.findAllActiveProducts();
            iterator = products.iterator();
        }
        return iterator.hasNext() ? iterator.next() : null;
    }
}
