package com.winestore.winestore.repository;

import com.winestore.winestore.entity.Cart;
import com.winestore.winestore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order,Long> {
    Optional<Order> findByUserId(Long userId);

    Optional<Order> findById(Long id);
}
