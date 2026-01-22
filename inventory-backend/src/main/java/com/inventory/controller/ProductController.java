package com.inventory.controller;

import com.inventory.dto.ApiResponse;
import com.inventory.dto.ProductRegisterRequest;
import com.inventory.model.product.Product;
import com.inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @GetMapping("/rfid/{uid}")
    public ApiResponse<Product> checkByUid(@PathVariable String uid) {

        Product product = productService.findByRfidTag(uid);

        if (product == null) {
            return ApiResponse.ok("NEW_PRODUCT", null);
        }
        return ApiResponse.ok("PRODUCT_FOUND", product);
    }


    @PostMapping
    public ApiResponse<Product> register(@RequestBody ProductRegisterRequest req) {

        Product product = new Product();
        product.setName(req.getName());
        product.setBarcode(req.getBarcode());
        product.setRfidTag(req.getRfidTag());
        product.setDescription(req.getDescription());
        product.setUnitWeight(req.getUnitWeight());

        Product saved = productService.registerProduct(product);
        return ApiResponse.ok("PRODUCT_CREATED", saved);
    }
}
