package com.inventory.controller;

import com.inventory.dto.ApiResponse;
import com.inventory.dto.RfidStockRequest;
import com.inventory.dto.RfidStoreEntryRequest;
import com.inventory.service.RfidEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rfid")
@RequiredArgsConstructor
public class RfidController {

    private final RfidEventService rfidEventService;


    @PostMapping("/stock/entry")
    public ApiResponse<Void> stockEntry(@RequestBody RfidStockRequest req) {

        int qty = (req.getQty() == null || req.getQty() <= 0) ? 1 : req.getQty();

        rfidEventService.handleStockEntry(req.getRfidTag(), req.getEsp32Id(), qty);
        return ApiResponse.ok("STOCK_ENTRY_OK", null);
    }


    @PostMapping("/stock/exit")
    public ApiResponse<Void> stockExit(@RequestBody RfidStockRequest req) {

        int qty = (req.getQty() == null || req.getQty() <= 0) ? 1 : req.getQty();

        rfidEventService.handleStockExit(req.getRfidTag(), req.getEsp32Id(), qty);
        return ApiResponse.ok("STOCK_EXIT_OK", null);
    }


    @PostMapping("/store/entry")
    public ApiResponse<Void> storeEntry(@RequestBody RfidStoreEntryRequest req) {

        int qty = (req.getQty() == null || req.getQty() <= 0) ? 1 : req.getQty();

        rfidEventService.handleStoreEntry(req.getRfidTag(), req.getEsp32Id(), req.getShelfId(), qty);
        return ApiResponse.ok("STORE_ENTRY_OK", null);
    }
}
