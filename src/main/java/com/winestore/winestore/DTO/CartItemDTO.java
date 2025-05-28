package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.CartItem;
import lombok.Data;

@Data
public class CartItemDTO {
        private Long cartItemId;
        private String productName;
        private int quantity;
        private double price;

        public CartItemDTO(CartItem cartItem) {
            this.cartItemId = cartItem.getId();
            this.productName = cartItem.getProduct().getName();
            this.quantity = cartItem.getQuantity();
            //   this.price=cartItem.getProduct().getPrice();
        }
}
