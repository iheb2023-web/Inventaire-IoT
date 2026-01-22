package com.inventory.service;

import com.inventory.dao.ProductDao;
import com.inventory.dao.RfidEventDao;
import com.inventory.dao.StockDao;
import com.inventory.dao.StoreStockDao;
import com.inventory.model.device.RfidEvent;
import com.inventory.model.product.Product;
import com.inventory.model.product.Stock;
import com.inventory.model.product.StoreStock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RfidEventService {

    private final ProductDao productDao;
    private final StockDao stockDao;
    private final StoreStockDao storeStockDao;
    private final RfidEventDao rfidEventDao;


    @Transactional
    public void handleStockEntry(String rfidTag, Long esp32Id, int qty) {

        Product product = productDao.findByRfidTag(rfidTag);
        if (product == null) {
            throw new RuntimeException("Produit inconnu. لازم تسجلو أول مرة من الفورم.");
        }

        RfidEvent event = new RfidEvent();
        event.setProductId(product.getId());
        event.setEventType(RfidEvent.EventType.ENTRY);
        event.setLocation(RfidEvent.EventLocation.STOCK);
        event.setEsp32Id(esp32Id);
        rfidEventDao.insert(event);

        Stock stock = stockDao.findByProductId(product.getId());
        if (stock == null) {
            Stock s = new Stock();
            s.setProductId(product.getId());
            s.setQuantity(qty);
            stockDao.insert(s);
        } else {
            stockDao.increase(product.getId(), qty);
        }
    }


    @Transactional
    public void handleStockExit(String rfidTag, Long esp32Id, int qty) {

        Product product = productDao.findByRfidTag(rfidTag);
        if (product == null) {
            throw new RuntimeException("Produit inconnu.");
        }

        RfidEvent event = new RfidEvent();
        event.setProductId(product.getId());
        event.setEventType(RfidEvent.EventType.EXIT);
        event.setLocation(RfidEvent.EventLocation.STOCK);
        event.setEsp32Id(esp32Id);
        rfidEventDao.insert(event);

        int updated = stockDao.decrease(product.getId(), qty);
        if (updated == 0) {
            throw new RuntimeException("Stock insuffisant!");
        }
    }


    @Transactional
    public void handleStoreEntry(String rfidTag, Long esp32Id, Long shelfId, int qty) {

        Product product = productDao.findByRfidTag(rfidTag);
        if (product == null) {
            throw new RuntimeException("Produit inconnu.");
        }

        RfidEvent event = new RfidEvent();
        event.setProductId(product.getId());
        event.setEventType(RfidEvent.EventType.ENTRY);
        event.setLocation(RfidEvent.EventLocation.STORE);
        event.setEsp32Id(esp32Id);
        rfidEventDao.insert(event);

        int updated = stockDao.decrease(product.getId(), qty);
        if (updated == 0) {
            throw new RuntimeException("Stock insuffisant pour transfert vers magasin!");
        }

        StoreStock ss = storeStockDao.findByProductAndShelf(product.getId(), shelfId);
        if (ss == null) {
            StoreStock newSS = new StoreStock();
            newSS.setProductId(product.getId());
            newSS.setShelfId(shelfId);
            newSS.setQuantity(qty);
            storeStockDao.insert(newSS);
        } else {
            storeStockDao.increase(product.getId(), shelfId, qty);
        }
    }
}