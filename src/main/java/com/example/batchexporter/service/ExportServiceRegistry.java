package com.example.batchexporter.service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ExportServiceRegistry {

    private final Map<String, ExportService> servicesByName;

    public ExportServiceRegistry(List<ExportService> services) {
        this.servicesByName = services.stream()
                .collect(Collectors.toMap(ExportService::getName, Function.identity()));
    }

    public ExportService getService(String name) {
        ExportService service = servicesByName.get(name);
        if (service == null) {
            throw new IllegalArgumentException(
                    "No ExportService registered for: " + name
                    + ". Available: " + servicesByName.keySet());
        }
        return service;
    }
}
