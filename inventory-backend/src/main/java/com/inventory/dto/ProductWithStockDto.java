package com.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductWithStockDto {
    private Long id;
    private String name;
    private String barcode;
    private String rfidTag;
    private String description;
    private BigDecimal unitWeight;
    private String createdAt;
    private Integer stockQuantity;
}
