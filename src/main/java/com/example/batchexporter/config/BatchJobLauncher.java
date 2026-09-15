package com.example.batchexporter.config;

import com.example.batchexporter.generator.FileGenerator;
import com.example.batchexporter.generator.FileGeneratorFactory;
import com.example.batchexporter.service.ExportService;
import com.example.batchexporter.service.ExportServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Set;

@Component
public class BatchJobLauncher implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(BatchJobLauncher.class);

    private final BatchExportProperties properties;
    private final ExportServiceRegistry exportServiceRegistry;
    private final FileGeneratorFactory fileGeneratorFactory;

    public BatchJobLauncher(BatchExportProperties properties,
                            ExportServiceRegistry exportServiceRegistry,
                            FileGeneratorFactory fileGeneratorFactory) {
        this.properties = properties;
        this.exportServiceRegistry = exportServiceRegistry;
        this.fileGeneratorFactory = fileGeneratorFactory;
    }

    @Override
    public void run(String... args) throws Exception {
        Set<String> exportsToRun = resolveExportsToRun(args);

        for (String exportName : exportsToRun) {
            log.info("Starting export: {}", exportName);

            BatchExportProperties.ExportDefinition def = properties.getExport(exportName);

            ExportService exportService = exportServiceRegistry.getService(def.getBean());
            FileGenerator generator = fileGeneratorFactory.getGenerator(def.getFileType());

            String outputDir = def.getOutputDir();
            String outputPath = outputDir + "/" + def.getOutputFile();

            File dir = new File(outputDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            Object data = exportService.fetchData();
            generator.generate(List.of(data), outputPath);

            log.info("Export completed: {} -> {}", exportName, outputPath);
        }
    }

    private Set<String> resolveExportsToRun(String[] args) {
        if (args.length == 0) {
            log.info("No export name specified, running all: {}", properties.getExports().keySet());
            return properties.getExports().keySet();
        }

        for (String arg : args) {
            if (!properties.getExports().containsKey(arg)) {
                throw new IllegalArgumentException(
                        "Unknown export: " + arg
                        + ". Available: " + properties.getExports().keySet());
            }
        }

        return Set.of(args);
    }
}
