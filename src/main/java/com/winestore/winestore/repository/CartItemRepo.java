package com.winestore.winestore.repository;

import com.winestore.winestore.entity.Cart;
import com.winestore.winestore.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItem,Long> {
//    Optional<User> findByUsername(String username);
}
