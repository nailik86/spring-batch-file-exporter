package com.example.batchexporter.service;

import com.example.batchexporter.dto.MagasinDto;
import com.example.batchexporter.dto.MagasinExportDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MagasinExportServiceTest {

    private final MagasinExportService service = new MagasinExportService();

    @Test
    void shouldReturnMagasinAsName() {
        assertThat(service.getName()).isEqualTo("magasin");
    }

    @Test
    void shouldReturnMagasinExportDto() {
        Object result = service.fetchData();
        assertThat(result).isInstanceOf(MagasinExportDto.class);
    }

    @Test
    void shouldContainMagasins() {
        MagasinExportDto dto = (MagasinExportDto) service.fetchData();
        assertThat(dto.getList()).isNotEmpty();
        assertThat(dto.getList()).hasSize(2);
    }

    @Test
    void shouldContainSuccursales() {
        MagasinExportDto dto = (MagasinExportDto) service.fetchData();
        MagasinDto first = dto.getList().get(0);
        assertThat(first.getMagasinId()).isEqualTo("ID1");
        assertThat(first.getMagasinsSuccursale()).hasSize(2);
    }
}
