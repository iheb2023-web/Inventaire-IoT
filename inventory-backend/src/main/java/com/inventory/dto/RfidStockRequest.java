package com.inventory.dto;

import lombok.Data;

@Data
public class RfidStockRequest {
    private String rfidTag;   // UID
    private String esp32Id;
    private Integer qty;      // default = 1
}
