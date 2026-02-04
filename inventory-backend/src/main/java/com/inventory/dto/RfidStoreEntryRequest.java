package com.inventory.dto;

import lombok.Data;

@Data
public class RfidStoreEntryRequest {
    private String rfidTag;   // UID
    private String esp32Id;
    private Long shelfId;     // shelf target
    private Integer qty;      // default = 1
}
