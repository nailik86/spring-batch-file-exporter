package com.example.batchexporter.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class JsonFileGenerator<T> implements FileGenerator<T> {

    private final ObjectMapper objectMapper;

    public JsonFileGenerator() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public void generate(List<T> items, String outputPath) throws IOException {
        objectMapper.writeValue(new File(outputPath), items);
    }

    @Override
    public String getFormat() {
        return "json";
    }
}
