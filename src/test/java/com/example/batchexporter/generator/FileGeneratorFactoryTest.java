package com.example.batchexporter.generator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileGeneratorFactoryTest {

    private FileGeneratorFactory factory;

    @BeforeEach
    void setUp() {
        factory = new FileGeneratorFactory(List.of(
                new JsonFileGenerator<>(),
                new CsvFileGenerator<>()
        ));
    }

    @Test
    void shouldReturnJsonGenerator() {
        FileGenerator<?> generator = factory.getGenerator("json");
        assertThat(generator).isInstanceOf(JsonFileGenerator.class);
    }

    @Test
    void shouldReturnCsvGenerator() {
        FileGenerator<?> generator = factory.getGenerator("csv");
        assertThat(generator).isInstanceOf(CsvFileGenerator.class);
    }

    @Test
    void shouldBeCaseInsensitive() {
        FileGenerator<?> generator = factory.getGenerator("JSON");
        assertThat(generator).isInstanceOf(JsonFileGenerator.class);
    }

    @Test
    void shouldThrowForUnknownFormat() {
        assertThatThrownBy(() -> factory.getGenerator("xml"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No FileGenerator registered for format: xml");
    }
}
