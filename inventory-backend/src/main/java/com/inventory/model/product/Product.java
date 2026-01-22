package com.inventory.model.product;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Product {
    private Long id;
    private String name;
    private String barcode;
    private String rfidTag;
    private String description;
    private BigDecimal unitWeight;
    private LocalDateTime createdAt;
}
