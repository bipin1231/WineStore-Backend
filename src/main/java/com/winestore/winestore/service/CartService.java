package com.winestore.winestore.service;

import com.winestore.winestore.DTO.CartDTO;
import com.winestore.winestore.DTO.CartItemDTO;
import com.winestore.winestore.entity.Cart;
import com.winestore.winestore.entity.CartItem;
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


    public Cart createOrUpdateCart(Long userId) {

        Optional<Cart> cart = cartRepo.findById(userId);
        if (cart.isPresent()) {
            return cart.get();
        } else {
            Cart newCart = new Cart();
            Optional<User> user = userRepo.findById(userId);
            if (user.isPresent()) {
                newCart.setUser(user.get());
                return cartRepo.save(newCart);
            }
            return null;
        }
    }

    public List<CartItemDTO> getCartItemByUserId(Long userId) {
        // Find the cart by user ID
        Optional<Cart> cartOptional = cartRepo.findByUserId(userId);

        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();

            // Get the cart item associated with this cart
            List<CartItem> cartItem = cartItemRepo.findAllByCartId(cart.getId()); // use findFirst if multiple items

            if (!cartItem.isEmpty()) {

                return cartItem.stream().map(CartItemDTO::new).collect(Collectors.toList());
            } else {
                throw new RuntimeException("No cart item found for this cart.");
            }
        } else {
            throw new RuntimeException("No cart found for this user.");
        }
    }

    public List<CartDTO> getAllCart(){
        return cartRepo.findAll().stream().map(cart->new CartDTO(cart)).collect(Collectors.toList());
    }
}
