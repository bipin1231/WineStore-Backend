package com.winestore.winestore.repository;

import com.winestore.winestore.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product,Long> {
//

    Optional<Product> findByName(String name);

    @EntityGraph(attributePaths = {"category"})
List<Product> findAll();
}
