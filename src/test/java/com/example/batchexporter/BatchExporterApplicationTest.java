package com.example.batchexporter;

import com.example.batchexporter.config.BatchExportProperties;
import com.example.batchexporter.config.BatchJobLauncher;
import com.example.batchexporter.generator.FileGeneratorFactory;
import com.example.batchexporter.service.ExportServiceRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BatchExporterApplicationTest {

    @Autowired
    private BatchJobLauncher batchJobLauncher;

    @Autowired
    private ExportServiceRegistry exportServiceRegistry;

    @Autowired
    private FileGeneratorFactory fileGeneratorFactory;

    @Autowired
    private BatchExportProperties batchExportProperties;

    @Test
    void contextLoads() {
        assertThat(batchJobLauncher).isNotNull();
        assertThat(exportServiceRegistry).isNotNull();
        assertThat(fileGeneratorFactory).isNotNull();
        assertThat(batchExportProperties).isNotNull();
    }

    @Test
    void shouldHaveExportsDefined() {
        assertThat(batchExportProperties.getExports()).containsKeys("magasin", "product");
    }

    @Test
    void shouldResolveExportServices() {
        assertThat(exportServiceRegistry.getService("magasin")).isNotNull();
        assertThat(exportServiceRegistry.getService("product")).isNotNull();
    }

    @Test
    void shouldResolveFileGenerators() {
        assertThat(fileGeneratorFactory.getGenerator("json")).isNotNull();
        assertThat(fileGeneratorFactory.getGenerator("csv")).isNotNull();
    }
}
