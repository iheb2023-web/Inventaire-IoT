package com.inventory.dao;

import com.inventory.model.product.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductDao {
    Product findById(@Param("id") Long id);

    Product findByRfidTag(@Param("rfidTag") String rfidTag);

    Product findByBarcode(@Param("barcode") String barcode);

    int insert(Product product);
}
