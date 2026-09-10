package com.example.batchexporter.config;

import com.example.batchexporter.dto.ProductDto;
import com.example.batchexporter.entity.Product;
import com.example.batchexporter.processor.ProductProcessor;
import com.example.batchexporter.reader.ProductReader;
import com.example.batchexporter.writer.FileExportWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    @Value("${batch.export.chunk-size:100}")
    private int chunkSize;

    @Value("${batch.export.output-dir}")
    private String outputDir;

    @Bean
    public Step exportStep(JobRepository jobRepository,
                           PlatformTransactionManager transactionManager,
                           ProductReader reader,
                           ProductProcessor processor,
                           FileExportWriter writer) {
        return new StepBuilder("exportStep", jobRepository)
                .<Product, ProductDto>chunk(chunkSize, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(new StepExecutionListenerSupport() {
                    @Override
                    public org.springframework.batch.core.ExitStatus afterStep(StepExecution stepExecution) {
                        try {
                            writer.flush();
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to flush file output", e);
                        }
                        return stepExecution.getExitStatus();
                    }
                })
                .build();
    }

    @Bean
    public Step createOutputDirStep(JobRepository jobRepository,
                                    PlatformTransactionManager transactionManager) {
        return new StepBuilder("createOutputDirStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    java.io.File dir = new java.io.File(outputDir);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Job exportJob(JobRepository jobRepository,
                         Step createOutputDirStep,
                         Step exportStep) {
        return new JobBuilder("productExportJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(createOutputDirStep)
                .next(exportStep)
                .build();
    }
}
