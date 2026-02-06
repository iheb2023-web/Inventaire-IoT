package com.inventory.dao;

import com.inventory.model.product.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ProductDao {
    Product findById(@Param("id") Long id);
    Product findByRfidTag(@Param("rfidTag") String rfidTag);
    Product findByBarcode(@Param("barcode") String barcode);
    int insert(Product product);

    long count();
    List<Product> findAll();

    void update(Product product);

    void delete(Long id);
}