package com.winestore.winestore.repository;

import com.winestore.winestore.entity.CartItem;
import com.winestore.winestore.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepo extends JpaRepository<OrderItem,Long> {
//    Optional<User> findByUsername(String username);
}
