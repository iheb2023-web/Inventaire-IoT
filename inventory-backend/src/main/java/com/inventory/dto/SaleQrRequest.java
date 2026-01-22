package com.inventory.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleQrRequest {
    private String barcode;         // scanned QR / barcode
    private Long shelfId;
    private Integer qty;
    private BigDecimal totalPrice;  // calculated by frontend or backend
}