package com.inventory.dao;

import com.inventory.model.product.Sale;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SaleDao {
    int insert(Sale sale);
}
