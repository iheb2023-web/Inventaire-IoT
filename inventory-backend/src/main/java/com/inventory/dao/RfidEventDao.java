package com.inventory.dao;

import com.inventory.model.device.RfidEvent;
import com.inventory.dto.RfidEventWithProductDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RfidEventDao {
    int insert(RfidEvent event);
    List<RfidEvent> findRecent(@Param("limit") int limit);
    List<RfidEventWithProductDto> findRecentWithProduct(@Param("limit") int limit);
    long countByType(@Param("eventType") String eventType);

    List<RfidEvent> findAll();
    void delete(Long id);

}
