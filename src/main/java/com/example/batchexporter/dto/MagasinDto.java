package com.example.batchexporter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MagasinDto {

    @JsonProperty("magasin_id")
    private String magasinId;

    @JsonProperty("magasins_succursale")
    private List<SuccursaleDto> magasinsSuccursale;
}
