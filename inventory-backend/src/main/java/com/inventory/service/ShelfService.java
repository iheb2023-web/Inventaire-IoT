package com.inventory.service;

import com.inventory.dao.AlertDao;
import com.inventory.dao.ShelfDao;
import com.inventory.model.device.Alert;
import com.inventory.model.store.Shelf;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShelfService {

    private final ShelfDao shelfDao;
    private final AlertDao alertDao;

    @Transactional
    public void updateShelfWeight(Long shelfId, double weight) {

        Shelf shelf = shelfDao.findById(shelfId);
        if (shelf == null) throw new RuntimeException("Shelf not found");

        shelfDao.updateCurrentWeight(shelfId, weight);

        if (weight < shelf.getMinThreshold().doubleValue()) {
            Alert open = alertDao.findOpenAlertByShelf(shelfId);
            if (open == null) {
                Alert alert = new Alert();
                alert.setShelfId(shelfId);
                alert.setAlertType(Alert.AlertType.LOW_WEIGHT);
                alert.setStatus(Alert.AlertStatus.OPEN);
                alertDao.insert(alert);
            }
        } else {
            Alert open = alertDao.findOpenAlertByShelf(shelfId);
            if (open != null) {
                alertDao.resolveAlert(open.getId());
            }
        }
    }
}
