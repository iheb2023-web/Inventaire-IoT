package com.inventory.dao;

import com.inventory.model.device.Alert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AlertDao {
    Alert findOpenAlertByShelf(@Param("shelfId") Long shelfId);

    int insert(Alert alert);

    int resolveAlert(@Param("alertId") Long alertId);
}
