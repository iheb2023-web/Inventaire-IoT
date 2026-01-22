package com.inventory.model.product;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StoreStock {
    private Long id;
    private Long productId;
    private Long shelfId;
    private Integer quantity;
    private LocalDateTime lastUpdated;
}
