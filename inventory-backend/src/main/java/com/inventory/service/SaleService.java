package com.inventory.service;

import com.inventory.dao.ProductDao;
import com.inventory.dao.SaleDao;
import com.inventory.dao.StoreStockDao;
import com.inventory.model.product.Product;
import com.inventory.model.product.Sale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final ProductDao productDao;
    private final StoreStockDao storeStockDao;
    private final SaleDao saleDao;

    @Transactional
    public void makeSaleByBarcode(String barcode, Long shelfId, int qty, BigDecimal totalPrice) {

        Product product = productDao.findByBarcode(barcode);
        if (product == null) throw new RuntimeException("Produit introuvable");

        int updated = storeStockDao.decrease(product.getId(), shelfId, qty);
        if (updated == 0) throw new RuntimeException("Stock magasin insuffisant!");

        Sale sale = new Sale();
        sale.setProductId(product.getId());
        sale.setQuantity(qty);
        sale.setTotalPrice(totalPrice);
        saleDao.insert(sale);
    }
}