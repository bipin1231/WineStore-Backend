package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.Cart;

import java.util.List;
import java.util.stream.Collectors;

public class CartDTO {
    private Long id;
    private Long userId;
    private List<CartItemDTO> cartItem;

public CartDTO(Cart cart){
    this.id=cart.getId();
    this.userId=cart.getUser().getId();
    this.cartItem=cart.getCartItem()
                        .stream()
                        .map(CartItemDTO::new)
                        .collect(Collectors.toList());
}




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CartItemDTO> getCartItem() {
        return cartItem;
    }

    public void setCartItem(List<CartItemDTO> cartItem) {
        this.cartItem = cartItem;
    }
}
