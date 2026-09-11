package com.example.batchexporter.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BatchJobLauncher implements CommandLineRunner {

    private final JobLauncher jobLauncher;
    private final Map<String, Job> exportJobs;
    private final BatchExportProperties properties;

    public BatchJobLauncher(JobLauncher jobLauncher,
                            Map<String, Job> exportJobs,
                            BatchExportProperties properties) {
        this.jobLauncher = jobLauncher;
        this.exportJobs = exportJobs;
        this.properties = properties;
    }

    @Override
    public void run(String... args) throws Exception {
        for (String exportName : properties.getExports().keySet()) {
            Job job = exportJobs.get(exportName);
            if (job == null) {
                throw new IllegalStateException("No job found for export: " + exportName);
            }

            JobParameters params = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .addString("exportName", exportName)
                    .toJobParameters();

            jobLauncher.run(job, params);
        }
    }
}
