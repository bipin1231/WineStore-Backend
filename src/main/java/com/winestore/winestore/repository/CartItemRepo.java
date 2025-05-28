package com.winestore.winestore.repository;

import com.winestore.winestore.entity.Cart;
import com.winestore.winestore.entity.CartItem;
import com.winestore.winestore.entity.Product;
import com.winestore.winestore.entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepo extends JpaRepository<CartItem,Long> {


    List<CartItem> findAllByCartId(Long cartId);
    Optional<CartItem> findByCartAndProductAndSize(Cart cart, Product product, String size);




}
