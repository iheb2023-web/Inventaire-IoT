package com.inventory.controller;

import com.inventory.dto.ApiResponse;
import com.inventory.dto.ProductRegisterRequest;
import com.inventory.dto.ProductWithStockDto;
import com.inventory.model.product.Product;
import com.inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        Product saved = productService.registerProduct(product, req.getEsp32Id());
        return ApiResponse.ok("PRODUCT_CREATED", saved);
    }

    @GetMapping
    public ApiResponse<List<Product>> getAll() {
        List<Product> products = productService.getAllProducts();
        return ApiResponse.ok("PRODUCTS_LIST", products);
    }

    @GetMapping("/with-stock")
    public ApiResponse<List<ProductWithStockDto>> getAllWithStock() {
        List<ProductWithStockDto> products = productService.getAllProductsWithStock();
        return ApiResponse.ok("PRODUCTS_WITH_STOCK", products);
    }

    @PutMapping("/{id}")
    public ApiResponse<Product> update(@PathVariable Long id, @RequestBody ProductRegisterRequest req) {
        Product existing = productService.findByRfidTag(req.getRfidTag());
        Product p = new Product();
        p.setId(id);
        p.setName(req.getName());
        p.setBarcode(req.getBarcode());
        p.setRfidTag(req.getRfidTag());
        p.setDescription(req.getDescription());
        p.setUnitWeight(req.getUnitWeight());

        Product updated = productService.updateProduct(p);
        return ApiResponse.ok("PRODUCT_UPDATED", updated);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ApiResponse.ok("PRODUCT_DELETED", null);
    }

}
