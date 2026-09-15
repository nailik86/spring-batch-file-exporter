package com.example.batchexporter.service;

import com.example.batchexporter.dto.MagasinDto;
import com.example.batchexporter.dto.MagasinExportDto;
import com.example.batchexporter.dto.SuccursaleDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MagasinExportService implements ExportService {

    @Override
    public String getName() {
        return "magasin";
    }

    @Override
    public Object fetchData() {
        // TODO: replace with magasinRepository.findAll() + mapping
        List<MagasinDto> magasins = List.of(
                MagasinDto.builder()
                        .magasinId("ID1")
                        .magasinsSuccursale(List.of(
                                SuccursaleDto.builder().id("id1").nom("Succursale A").build(),
                                SuccursaleDto.builder().id("id2").nom("Succursale B").build()
                        ))
                        .build(),
                MagasinDto.builder()
                        .magasinId("ID2")
                        .magasinsSuccursale(List.of(
                                SuccursaleDto.builder().id("id3").nom("Succursale C").build()
                        ))
                        .build()
        );

        return MagasinExportDto.builder().list(magasins).build();
    }
}
