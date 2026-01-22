package com.inventory.dto;

import lombok.Data;

@Data
public class ShelfWeightRequest {
    private Long shelfId;
    private Long esp32Id;        // optional (if you want log later)
    private Double currentWeight;
}
