package com.inventory.dao;

import com.inventory.model.device.RfidEvent;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RfidEventDao {
    int insert(RfidEvent event);

}
