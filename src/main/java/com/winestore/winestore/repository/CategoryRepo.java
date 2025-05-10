package com.winestore.winestore.repository;

import com.winestore.winestore.entity.Category;
import com.winestore.winestore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category,Long> {
    Optional<Category> findByName(String name);
}
