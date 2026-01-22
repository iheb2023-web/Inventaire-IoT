package com.inventory.model.product;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Stock {
    private Long id;
    private Long productId;
    private Integer quantity;
    private LocalDateTime lastUpdated;
}
