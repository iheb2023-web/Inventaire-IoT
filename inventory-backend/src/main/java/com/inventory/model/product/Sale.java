package com.inventory.model.product;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Sale {
    private Long id;
    private Long productId;
    private Integer quantity;
    private BigDecimal totalPrice;
    private LocalDateTime soldAt;
}
