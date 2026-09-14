package com.example.batchexporter.generator;

import java.io.IOException;
import java.util.List;

public interface FileGenerator {

    void generate(List<?> items, String outputPath) throws IOException;

    String getFormat();
}
