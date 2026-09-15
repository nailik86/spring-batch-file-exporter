package com.example.batchexporter.writer;

import com.example.batchexporter.generator.FileGenerator;
import com.example.batchexporter.service.ExportService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.List;

public class GenericExportWriter implements Tasklet {

    private final ExportService exportService;
    private final FileGenerator fileGenerator;
    private final String outputPath;

    public GenericExportWriter(ExportService exportService,
                               FileGenerator fileGenerator,
                               String outputPath) {
        this.exportService = exportService;
        this.fileGenerator = fileGenerator;
        this.outputPath = outputPath;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Object data = exportService.fetchData();
        fileGenerator.generate(List.of(data), outputPath);
        return RepeatStatus.FINISHED;
    }
}
