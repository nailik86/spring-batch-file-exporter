package com.example.batchexporter.service;

import com.example.batchexporter.dto.MagasinDto;
import com.example.batchexporter.dto.MagasinExportDto;
import com.example.batchexporter.dto.SuccursaleDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MagasinExportService implements ExportService {

    // TODO: inject a MagasinRepository here for real data
    @Override
    public String getName() {
        return "magasin";
    }

    @Override
    public List<?> fetchData() {
        // Replace with repository call: magasinRepository.findAll()
        return List.of(
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
    }

    @Override
    public Object wrapForExport(List<?> items) {
        @SuppressWarnings("unchecked")
        List<MagasinDto> magasins = (List<MagasinDto>) items;
        return MagasinExportDto.builder().list(magasins).build();
    }
}
