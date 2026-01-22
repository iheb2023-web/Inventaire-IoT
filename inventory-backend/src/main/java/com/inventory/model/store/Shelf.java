package com.inventory.model.store;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class Shelf {
    private Long id;
    private String name;
    private BigDecimal maxWeight;
    private BigDecimal minThreshold;
    private BigDecimal currentWeight;
}
