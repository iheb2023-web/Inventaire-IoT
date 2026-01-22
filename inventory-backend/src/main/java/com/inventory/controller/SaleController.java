package com.inventory.controller;

import com.inventory.dto.ApiResponse;
import com.inventory.dto.SaleQrRequest;
import com.inventory.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @PostMapping("/qr")
    public ApiResponse<Void> saleByQr(@RequestBody SaleQrRequest req) {

        int qty = (req.getQty() == null || req.getQty() <= 0) ? 1 : req.getQty();

        saleService.makeSaleByBarcode(req.getBarcode(), req.getShelfId(), qty, req.getTotalPrice());
        return ApiResponse.ok("SALE_CREATED", null);
    }
}