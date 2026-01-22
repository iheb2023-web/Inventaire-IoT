package com.inventory.dao;

import com.inventory.model.store.Shelf;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShelfDao {
    Shelf findById(@Param("id") Long id);

    int updateCurrentWeight(@Param("shelfId") Long shelfId, @Param("currentWeight") double currentWeight);
}
