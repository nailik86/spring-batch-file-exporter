package com.example.batchexporter.writer;

import com.example.batchexporter.dto.MagasinDto;
import com.example.batchexporter.dto.MagasinExportDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component("magasinFileExportWriter")
public class MagasinFileExportWriter implements ItemWriter<MagasinDto> {

    private final ObjectMapper objectMapper;
    private final String outputDir;
    private final List<MagasinDto> buffer = new ArrayList<>();

    public MagasinFileExportWriter(@Value("${batch.export.output-dir}") String outputDir) {
        this.outputDir = outputDir;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public void write(Chunk<? extends MagasinDto> chunk) {
        buffer.addAll(chunk.getItems());
    }

    public void flush() throws Exception {
        MagasinExportDto export = MagasinExportDto.builder()
                .list(new ArrayList<>(buffer))
                .build();
        objectMapper.writeValue(new File(outputDir + "/magasins_export.json"), export);
        buffer.clear();
    }
}
