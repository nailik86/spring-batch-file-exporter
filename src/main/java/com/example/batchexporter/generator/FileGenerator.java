package com.example.batchexporter.generator;

import java.io.IOException;

public interface FileGenerator {

    void generate(Object data, String outputPath) throws IOException;

    String getFormat();
}
