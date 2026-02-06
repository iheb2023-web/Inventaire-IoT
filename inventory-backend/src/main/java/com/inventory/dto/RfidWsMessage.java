package com.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RfidWsMessage {
    private String type;     // NEW_PRODUCT, STOCK_UPDATED, ERROR
    private String rfidTag;
    private String location; // STOCK / STORE
}
