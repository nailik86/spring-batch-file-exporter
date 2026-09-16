package com.example.batchexporter.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class JsonFileGenerator implements FileGenerator {

    private final ObjectMapper objectMapper;

    public JsonFileGenerator() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public void generate(Object data, String outputPath) throws IOException {
        objectMapper.writeValue(new File(outputPath), data);
    }

    @Override
    public String getFormat() {
        return "json";
    }
}
