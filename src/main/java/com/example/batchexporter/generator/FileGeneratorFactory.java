package com.example.batchexporter.generator;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FileGeneratorFactory {

    private final Map<String, FileGenerator<?>> generatorsByFormat;

    public FileGeneratorFactory(List<FileGenerator<?>> generators) {
        this.generatorsByFormat = generators.stream()
                .collect(Collectors.toMap(FileGenerator::getFormat, Function.identity()));
    }

    @SuppressWarnings("unchecked")
    public <T> FileGenerator<T> getGenerator(String format) {
        FileGenerator<T> generator = (FileGenerator<T>) generatorsByFormat.get(format.toLowerCase());
        if (generator == null) {
            throw new IllegalArgumentException(
                    "No FileGenerator registered for format: " + format
                    + ". Available formats: " + generatorsByFormat.keySet());
        }
        return generator;
    }
}
