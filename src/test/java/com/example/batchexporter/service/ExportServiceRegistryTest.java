package com.example.batchexporter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExportServiceRegistryTest {

    private ExportServiceRegistry registry;

    @BeforeEach
    void setUp() {
        ExportService serviceA = new ExportService() {
            @Override
            public String getName() { return "serviceA"; }
            @Override
            public Object fetchData() { return "dataA"; }
        };
        ExportService serviceB = new ExportService() {
            @Override
            public String getName() { return "serviceB"; }
            @Override
            public Object fetchData() { return "dataB"; }
        };
        registry = new ExportServiceRegistry(List.of(serviceA, serviceB));
    }

    @Test
    void shouldReturnServiceByName() {
        ExportService service = registry.getService("serviceA");
        assertThat(service.getName()).isEqualTo("serviceA");
        assertThat(service.fetchData()).isEqualTo("dataA");
    }

    @Test
    void shouldReturnDifferentServiceByName() {
        ExportService service = registry.getService("serviceB");
        assertThat(service.getName()).isEqualTo("serviceB");
    }

    @Test
    void shouldThrowForUnknownService() {
        assertThatThrownBy(() -> registry.getService("unknown"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No ExportService registered for: unknown");
    }
}
