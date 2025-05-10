package com.winestore.winestore.repository;

import com.winestore.winestore.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepo extends JpaRepository<Stock,Long> {

}
