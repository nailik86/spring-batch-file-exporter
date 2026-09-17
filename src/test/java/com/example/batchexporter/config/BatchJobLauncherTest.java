package com.example.batchexporter.config;

import com.example.batchexporter.generator.FileGenerator;
import com.example.batchexporter.generator.FileGeneratorFactory;
import com.example.batchexporter.service.ExportService;
import com.example.batchexporter.service.ExportServiceRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BatchJobLauncherTest {

    private BatchExportProperties properties;
    private List<String> generatedFiles;
    private List<Object> generatedData;

    @TempDir
    Path tempDir;

    private BatchJobLauncher launcher;

    @BeforeEach
    void setUp() {
        generatedFiles = new ArrayList<>();
        generatedData = new ArrayList<>();

        ExportService magasinService = new ExportService() {
            @Override
            public String getName() { return "magasin"; }
            @Override
            public Object fetchData() { return Map.of("list", List.of("mag1", "mag2")); }
        };

        FileGenerator jsonGenerator = new FileGenerator() {
            @Override
            public void generate(Object data, String outputPath) {
                generatedData.add(data);
                generatedFiles.add(outputPath);
            }
            @Override
            public String getFormat() { return "json"; }
        };

        ExportServiceRegistry registry = new ExportServiceRegistry(List.of(magasinService));
        FileGeneratorFactory factory = new FileGeneratorFactory(List.of(jsonGenerator));

        properties = new BatchExportProperties();
        BatchExportProperties.ExportDefinition def = new BatchExportProperties.ExportDefinition();
        def.setBean("magasin");
        def.setFileType("json");
        def.setOutputDir(tempDir.resolve("output").toString());
        def.setOutputFile("magasins.json");
        properties.setExports(Map.of("magasin", def));

        launcher = new BatchJobLauncher(properties, registry, factory);
    }

    @Test
    void shouldRunSpecifiedExport() throws Exception {
        launcher.run("magasin");

        assertThat(generatedFiles).hasSize(1);
        assertThat(generatedFiles.get(0)).endsWith("magasins.json");
        assertThat(generatedData.get(0)).isEqualTo(Map.of("list", List.of("mag1", "mag2")));
    }

    @Test
    void shouldRunAllExportsWhenNoArgs() throws Exception {
        launcher.run();

        assertThat(generatedFiles).hasSize(1);
    }

    @Test
    void shouldCreateOutputDirectory() throws Exception {
        launcher.run("magasin");

        assertThat(tempDir.resolve("output")).exists();
        assertThat(tempDir.resolve("output")).isDirectory();
    }

    @Test
    void shouldThrowForUnknownExport() {
        assertThatThrownBy(() -> launcher.run("unknown"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown export: unknown");
    }
}
