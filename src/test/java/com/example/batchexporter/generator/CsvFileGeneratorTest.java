package com.example.batchexporter.generator;

import com.example.batchexporter.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CsvFileGeneratorTest {

    private final CsvFileGenerator<ProductDto> generator = new CsvFileGenerator<>();

    @Test
    void shouldGenerateCsvFile(@TempDir Path tempDir) throws Exception {
        String outputPath = tempDir.resolve("test.csv").toString();
        List<ProductDto> items = List.of(
                ProductDto.builder()
                        .id(1L)
                        .name("LAPTOP")
                        .category("Electronics")
                        .unitPrice(new BigDecimal("999.99"))
                        .stockQuantity(10)
                        .totalValue(new BigDecimal("9999.90"))
                        .build()
        );

        generator.generate(items, outputPath);

        List<String> lines = Files.readAllLines(Path.of(outputPath));
        assertThat(lines).hasSize(2);
        assertThat(lines.get(0)).contains("id", "name", "category");
        assertThat(lines.get(1)).contains("LAPTOP", "Electronics", "999.99");
    }

    @Test
    void shouldReturnCsvFormat() {
        assertThat(generator.getFormat()).isEqualTo("csv");
    }
}
