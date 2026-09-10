package com.example.batchexporter.generator;

import java.io.IOException;
import java.util.List;

public interface FileGenerator<T> {

    void generate(List<T> items, String outputPath) throws IOException;

    String getFormat();
}
