package com.example.batchexporter.writer;

import com.example.batchexporter.dto.ProductDto;
import com.example.batchexporter.generator.FileGenerator;
import com.example.batchexporter.generator.FileGeneratorFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FileExportWriter implements ItemWriter<ProductDto> {

    private final FileGeneratorFactory fileGeneratorFactory;
    private final String outputFormat;
    private final String outputDir;
    private final List<ProductDto> buffer = new ArrayList<>();

    public FileExportWriter(
            FileGeneratorFactory fileGeneratorFactory,
            @Value("${batch.export.format}") String outputFormat,
            @Value("${batch.export.output-dir}") String outputDir) {
        this.fileGeneratorFactory = fileGeneratorFactory;
        this.outputFormat = outputFormat;
        this.outputDir = outputDir;
    }

    @Override
    public void write(Chunk<? extends ProductDto> chunk) {
        buffer.addAll(chunk.getItems());
    }

    public void flush() throws Exception {
        FileGenerator<ProductDto> generator = fileGeneratorFactory.getGenerator(outputFormat);
        String outputPath = outputDir + "/products_export." + outputFormat;
        generator.generate(buffer, outputPath);
        buffer.clear();
    }
}
