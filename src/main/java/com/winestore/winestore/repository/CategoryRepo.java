package com.winestore.winestore.repository;

import com.winestore.winestore.entity.Category;
import com.winestore.winestore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepo extends JpaRepository<Category,Long> {

    List<Category> findByParentIsNull();

    Optional<Category> findByName(String name);
}
