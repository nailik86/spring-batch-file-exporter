package com.example.batchexporter.generator;

import com.opencsv.CSVWriter;
import com.example.batchexporter.dto.ProductDto;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component
public class CsvFileGenerator<T> implements FileGenerator<T> {

    @Override
    @SuppressWarnings("unchecked")
    public void generate(List<T> items, String outputPath) throws IOException {
        List<ProductDto> products = (List<ProductDto>) items;
        try (CSVWriter writer = new CSVWriter(new FileWriter(outputPath))) {
            writer.writeNext(new String[]{"id", "name", "category", "unit_price", "stock_quantity", "total_value"});
            for (ProductDto p : products) {
                writer.writeNext(new String[]{
                        String.valueOf(p.getId()),
                        p.getName(),
                        p.getCategory(),
                        p.getUnitPrice().toPlainString(),
                        String.valueOf(p.getStockQuantity()),
                        p.getTotalValue().toPlainString()
                });
            }
        }
    }

    @Override
    public String getFormat() {
        return "csv";
    }
}
