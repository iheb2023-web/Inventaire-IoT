package com.inventory.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRegisterRequest {
    private String name;
    private String barcode;
    private String rfidTag;       // UID coming from RFID
    private String description;
    private BigDecimal unitWeight;
    private String esp32Id;

}