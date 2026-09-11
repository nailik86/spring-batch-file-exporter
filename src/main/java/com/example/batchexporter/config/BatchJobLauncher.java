package com.example.batchexporter.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class BatchJobLauncher implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(BatchJobLauncher.class);

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
        Set<String> jobsToRun = resolveJobsToRun(args);

        for (String exportName : jobsToRun) {
            Job job = exportJobs.get(exportName);
            if (job == null) {
                throw new IllegalStateException(
                        "No job found for: " + exportName
                        + ". Available: " + exportJobs.keySet());
            }

            log.info("Starting export job: {}", exportName);

            JobParameters params = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .addString("exportName", exportName)
                    .toJobParameters();

            jobLauncher.run(job, params);
        }
    }

    private Set<String> resolveJobsToRun(String[] args) {
        if (args.length == 0) {
            log.info("No export name specified, running all: {}", properties.getExports().keySet());
            return properties.getExports().keySet();
        }

        for (String arg : args) {
            if (!properties.getExports().containsKey(arg)) {
                throw new IllegalArgumentException(
                        "Unknown export: " + arg
                        + ". Available: " + properties.getExports().keySet());
            }
        }

        return Set.of(args);
    }
}
