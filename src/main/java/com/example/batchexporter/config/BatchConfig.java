package com.example.batchexporter.config;

import com.example.batchexporter.generator.FileGeneratorFactory;
import com.example.batchexporter.reader.GenericExportReader;
import com.example.batchexporter.service.ExportService;
import com.example.batchexporter.service.ExportServiceRegistry;
import com.example.batchexporter.writer.GenericExportWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.File;
import java.util.Map;

@Configuration
public class BatchConfig {

    private final BatchExportProperties properties;
    private final ExportServiceRegistry exportServiceRegistry;
    private final FileGeneratorFactory fileGeneratorFactory;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public BatchConfig(BatchExportProperties properties,
                       ExportServiceRegistry exportServiceRegistry,
                       FileGeneratorFactory fileGeneratorFactory,
                       JobRepository jobRepository,
                       PlatformTransactionManager transactionManager) {
        this.properties = properties;
        this.exportServiceRegistry = exportServiceRegistry;
        this.fileGeneratorFactory = fileGeneratorFactory;
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Map<String, Job> exportJobs() {
        Map<String, BatchExportProperties.ExportDefinition> exports = properties.getExports();
        return exports.entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> buildJob(entry.getKey(), entry.getValue())
                ));
    }

    private Job buildJob(String name, BatchExportProperties.ExportDefinition def) {
        ExportService exportService = exportServiceRegistry.getService(def.getBean());

        String outputDir = def.getOutputDir();
        String outputPath = outputDir + "/" + def.getOutputFile();

        Step createDirStep = new StepBuilder(name + "_createDir", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    File dir = new File(outputDir);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();

        GenericExportWriter writer = new GenericExportWriter(
                exportService,
                fileGeneratorFactory.getGenerator(def.getFileType()),
                outputPath
        );

        Step exportStep = new StepBuilder(name + "_export", jobRepository)
                .<Object, Object>chunk(100, transactionManager)
                .reader(new GenericExportReader(exportService))
                .writer(writer)
                .listener(new StepExecutionListenerSupport() {
                    @Override
                    public org.springframework.batch.core.ExitStatus afterStep(StepExecution stepExecution) {
                        try {
                            writer.flush();
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to flush export: " + name, e);
                        }
                        return stepExecution.getExitStatus();
                    }
                })
                .build();

        return new JobBuilder(name + "ExportJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(createDirStep)
                .next(exportStep)
                .build();
    }
}
