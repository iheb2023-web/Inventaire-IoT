package com.inventory.controller;

import com.inventory.dao.ProductDao;
import com.inventory.dao.RfidEventDao;
import com.inventory.dao.StockDao;
import com.inventory.dao.StoreStockDao;
import com.inventory.dto.ApiResponse;
import com.inventory.dto.RfidStockRequest;
import com.inventory.dto.RfidStoreEntryRequest;
import com.inventory.dto.RfidEventWithProductDto;
import com.inventory.model.device.RfidEvent;
import com.inventory.service.RfidEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rfid")
@RequiredArgsConstructor
public class RfidController {

    private final RfidEventService rfidEventService;
    private final ProductDao productDao;
    private final StoreStockDao storeStockDao;
    private final RfidEventDao rfidEventDao;
    private final StockDao stockDao;

    @GetMapping("/movements")
    public ApiResponse<List<RfidEvent>> getAllMovements() {
        List<RfidEvent> events = rfidEventService.getAllEvents();
        return ApiResponse.ok("ALL_EVENTS", events);
    }

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

    @GetMapping("/events/recent")
    public ApiResponse<List<RfidEvent>> getRecentEvents(@RequestParam(defaultValue = "20") int limit) {
        List<RfidEvent> events = rfidEventDao.findRecent(limit);
        return ApiResponse.ok("RECENT_EVENTS", events);
    }

    @GetMapping("/events/recent-with-product")
    public ApiResponse<List<RfidEventWithProductDto>> getRecentEventsWithProduct(@RequestParam(defaultValue = "20") int limit) {
        List<RfidEventWithProductDto> events = rfidEventDao.findRecentWithProduct(limit);
        return ApiResponse.ok("RECENT_EVENTS_WITH_PRODUCT", events);
    }

    @GetMapping("/stats")
    public ApiResponse<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProducts", productDao.count());
        stats.put("totalStock", stockDao.sumQuantity());
        stats.put("totalStoreStock", storeStockDao.sumQuantity());
        return ApiResponse.ok("STATS", stats);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        rfidEventService.deleteEvent(id);
        return ApiResponse.ok("Event Deleted", null);
    }

}
