package com.example.batchexporter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private Long id;

    private String name;

    private String category;

    @JsonProperty("unit_price")
    private BigDecimal unitPrice;

    @JsonProperty("stock_quantity")
    private Integer stockQuantity;

    @JsonProperty("total_value")
    private BigDecimal totalValue;
}
