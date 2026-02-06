package com.inventory.dao;

import com.inventory.model.product.StoreStock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StoreStockDao {
    StoreStock findByProductAndShelf(@Param("productId") Long productId, @Param("shelfId") Long shelfId);

    int insert(StoreStock storeStock);

    int increase(@Param("productId") Long productId, @Param("shelfId") Long shelfId, @Param("qty") int qty);

    int decrease(@Param("productId") Long productId, @Param("shelfId") Long shelfId, @Param("qty") int qty);

    long sumQuantity();
    List<StoreStock> findAll();

}
