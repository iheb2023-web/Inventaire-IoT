package com.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RfidEventWithProductDto {
    private Long id;
    private Long productId;
    private String productName;
    private String eventType;
    private String location;
    private String esp32Id;
    private String createdAt;
}
