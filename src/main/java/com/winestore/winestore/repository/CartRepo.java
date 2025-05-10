package com.winestore.winestore.repository;

import com.winestore.winestore.entity.Cart;
import com.winestore.winestore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart,Long> {
    Optional<Cart> findByUserId(Long userId);

    Optional<Cart> findById(Long id);
}
