package com.inventory.dao;

import com.inventory.model.product.Stock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StockDao {
    Stock findByProductId(@Param("productId") Long productId);

    int insert(Stock stock);

    int increase(@Param("productId") Long productId, @Param("qty") int qty);

    int decrease(@Param("productId") Long productId, @Param("qty") int qty);

    List<Stock> getAllStocks();

    long sumQuantity();
    List<Stock> findAll();
}
