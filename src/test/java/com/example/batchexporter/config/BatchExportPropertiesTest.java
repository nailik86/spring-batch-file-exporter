package com.example.batchexporter.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BatchExportPropertiesTest {

    private BatchExportProperties properties;

    @BeforeEach
    void setUp() {
        properties = new BatchExportProperties();

        BatchExportProperties.ExportDefinition magasinDef = new BatchExportProperties.ExportDefinition();
        magasinDef.setBean("magasin");
        magasinDef.setFileType("json");
        magasinDef.setOutputDir("./output/magasins");
        magasinDef.setOutputFile("magasins_export.json");

        BatchExportProperties.ExportDefinition productDef = new BatchExportProperties.ExportDefinition();
        productDef.setBean("product");
        productDef.setFileType("csv");
        productDef.setOutputDir("./output/products");
        productDef.setOutputFile("products_export.csv");

        properties.setExports(Map.of("magasin", magasinDef, "product", productDef));
    }

    @Test
    void shouldReturnExportDefinition() {
        BatchExportProperties.ExportDefinition def = properties.getExport("magasin");
        assertThat(def.getBean()).isEqualTo("magasin");
        assertThat(def.getFileType()).isEqualTo("json");
        assertThat(def.getOutputDir()).isEqualTo("./output/magasins");
        assertThat(def.getOutputFile()).isEqualTo("magasins_export.json");
    }

    @Test
    void shouldReturnAllExports() {
        assertThat(properties.getExports()).hasSize(2);
        assertThat(properties.getExports()).containsKeys("magasin", "product");
    }

    @Test
    void shouldThrowForUnknownExport() {
        assertThatThrownBy(() -> properties.getExport("unknown"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No export configuration found for: unknown");
    }
}
