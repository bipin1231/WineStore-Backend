package com.winestore.winestore.repository;

import com.winestore.winestore.entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductSizeRepo extends JpaRepository<ProductSize,Long> {
    Optional<ProductSize> findById(Long id);
}
