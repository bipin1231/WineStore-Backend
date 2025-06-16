package com.winestore.winestore.repository;

import com.winestore.winestore.entity.ProductVariant;
import com.winestore.winestore.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SizeRepo extends JpaRepository<Size,Long> {

    Optional<Size> findBySize(String size);
}
