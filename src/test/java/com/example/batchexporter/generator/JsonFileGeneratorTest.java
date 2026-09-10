package com.example.batchexporter.generator;

import com.example.batchexporter.dto.ProductDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JsonFileGeneratorTest {

    private final JsonFileGenerator<ProductDto> generator = new JsonFileGenerator<>();

    @Test
    void shouldGenerateJsonFile(@TempDir Path tempDir) throws Exception {
        String outputPath = tempDir.resolve("test.json").toString();
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

        ObjectMapper mapper = new ObjectMapper();
        List<ProductDto> result = mapper.readValue(
                new java.io.File(outputPath),
                new TypeReference<>() {}
        );
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("LAPTOP");
        assertThat(result.get(0).getTotalValue()).isEqualByComparingTo("9999.90");
    }

    @Test
    void shouldReturnJsonFormat() {
        assertThat(generator.getFormat()).isEqualTo("json");
    }
}
