package com.example.batchexporter.writer;

import com.example.batchexporter.generator.FileGenerator;
import com.example.batchexporter.service.ExportService;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.ArrayList;
import java.util.List;

public class GenericExportWriter implements ItemWriter<Object> {

    private final ExportService exportService;
    private final FileGenerator<Object> fileGenerator;
    private final String outputPath;
    private final List<Object> buffer = new ArrayList<>();

    public GenericExportWriter(ExportService exportService,
                               FileGenerator<Object> fileGenerator,
                               String outputPath) {
        this.exportService = exportService;
        this.fileGenerator = fileGenerator;
        this.outputPath = outputPath;
    }

    @Override
    public void write(Chunk<?> chunk) {
        buffer.addAll(chunk.getItems());
    }

    public void flush() throws Exception {
        Object wrapped = exportService.wrapForExport(buffer);
        fileGenerator.generate(List.of(wrapped), outputPath);
        buffer.clear();
    }
}
