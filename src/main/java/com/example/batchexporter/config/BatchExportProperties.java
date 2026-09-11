package com.example.batchexporter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "batch")
@Getter
@Setter
public class BatchExportProperties {

    private Map<String, ExportDefinition> exports;

    @Getter
    @Setter
    public static class ExportDefinition {
        private String bean;
        private String fileType;
        private String outputDir;
        private String outputFile;
    }

    public ExportDefinition getExport(String name) {
        ExportDefinition def = exports.get(name);
        if (def == null) {
            throw new IllegalArgumentException(
                    "No export configuration found for: " + name
                    + ". Available: " + exports.keySet());
        }
        return def;
    }
}
