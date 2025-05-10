package com.winestore.winestore.service;

import com.winestore.winestore.DTO.CartDTO;
import com.winestore.winestore.entity.Cart;
import com.winestore.winestore.entity.User;
import com.winestore.winestore.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Repository
public class CartService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CartItemRepo cartItemRepo;
    @Autowired
    private CartRepo cartRepo;


    public Cart createOrUpdateCart(String username) {
        Long userId = userRepo.findByUsername(username).get().getId();

        Optional<Cart> cart = cartRepo.findById(userId);
        if (cart.isPresent()) {
            return cart.get();
        } else {
            Cart newCart = new Cart();
            Optional<User> user = userRepo.findByUsername(username);
            if (user.isPresent()) {
                newCart.setUser(user.get());
                return cartRepo.save(newCart);
            }
            return null;
        }
    }

    public Optional<Cart> getCartById(Long id){
        return cartRepo.findById(id);
    }
    public List<CartDTO> getAllCart(){
        return cartRepo.findAll().stream().map(cart->new CartDTO(cart)).collect(Collectors.toList());
    }
}
