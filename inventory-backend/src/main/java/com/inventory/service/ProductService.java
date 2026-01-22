package com.inventory.service;

import com.inventory.dao.ProductDao;
import com.inventory.model.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductDao productDao;

    public Product findByRfidTag(String uid) {
        return productDao.findByRfidTag(uid);
    }

    public Product registerProduct(Product product) {
        productDao.insert(product);
        return product;
    }
}
