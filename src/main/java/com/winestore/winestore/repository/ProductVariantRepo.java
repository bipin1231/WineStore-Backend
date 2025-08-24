package com.winestore.winestore.repository;

import com.winestore.winestore.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductVariantRepo extends JpaRepository<ProductVariant,Long> {
//    Optional<ProductVariant> findBySize(String size);

    //Optional<ProductVariant> findBySizeAndProductId(String size, Long productId);
}
