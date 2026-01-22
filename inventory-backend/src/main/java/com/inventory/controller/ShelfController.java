package com.inventory.controller;

import com.inventory.dto.ApiResponse;
import com.inventory.dto.ShelfWeightRequest;
import com.inventory.service.ShelfService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shelf")
@RequiredArgsConstructor
public class ShelfController {

    private final ShelfService shelfService;


    @PostMapping("/weight")
    public ApiResponse<Void> updateWeight(@RequestBody ShelfWeightRequest req) {

        shelfService.updateShelfWeight(req.getShelfId(), req.getCurrentWeight());
        return ApiResponse.ok("WEIGHT_UPDATED", null);
    }
}
