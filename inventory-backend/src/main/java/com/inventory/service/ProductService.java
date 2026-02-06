package com.inventory.service;

import com.inventory.dao.ProductDao;
import com.inventory.dao.RfidEventDao;
import com.inventory.dao.StockDao;
import com.inventory.model.device.RfidEvent;
import com.inventory.model.product.Product;
import com.inventory.model.product.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductDao productDao;
    private final StockDao stockDao;
    private final RfidEventDao rfidEventDao;

    public Product findByRfidTag(String uid) {
        return productDao.findByRfidTag(uid);
    }

    @Transactional
    public Product registerProduct(Product product, String esp32Id) {

        productDao.insert(product);

        // ENTRY automatique après création
        RfidEvent event = new RfidEvent();
        event.setProductId(product.getId());
        event.setEventType(RfidEvent.EventType.ENTRY);
        event.setLocation(RfidEvent.EventLocation.STOCK);
        event.setEsp32Id(esp32Id);
        rfidEventDao.insert(event);

        Stock stock = new Stock();
        stock.setProductId(product.getId());
        stock.setQuantity(1);
        stockDao.insert(stock);

        return product;
    }


    @Transactional
    public Product updateProduct(Product product) {
        productDao.update(product);
        return product;
    }

    @Transactional
    public void deleteProduct(Long id) {
        productDao.delete(id);
    }

    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    public List<com.inventory.dto.ProductWithStockDto> getAllProductsWithStock() {
        List<Product> products = productDao.findAll();
        return products.stream()
                .map(p -> {
                    Stock stock = stockDao.findByProductId(p.getId());
                    Integer quantity = (stock != null) ? stock.getQuantity() : 0;
                    com.inventory.dto.ProductWithStockDto dto = new com.inventory.dto.ProductWithStockDto();
                    dto.setId(p.getId());
                    dto.setName(p.getName());
                    dto.setBarcode(p.getBarcode());
                    dto.setRfidTag(p.getRfidTag());
                    dto.setDescription(p.getDescription());
                    dto.setUnitWeight(p.getUnitWeight());
                    dto.setCreatedAt(p.getCreatedAt().toString());
                    dto.setStockQuantity(quantity);
                    return dto;
                })
                .toList();
    }

}
