package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.CartItem;
import lombok.Data;

@Data
public class CartItemDTO {
        private Long cartItemId;
        private String productName;
        private int quantity;
        private double totalPrice;
    private double productPrice;
        private String size;
        private String url;
        public CartItemDTO(CartItem cartItem) {
            this.cartItemId = cartItem.getId();
            this.productName = cartItem.getProduct().getName();
            this.quantity = cartItem.getQuantity();
            this.size=cartItem.getProductSize().getSize();
            this.productPrice=cartItem.getProductSize().getSellingPrice();
            this.totalPrice=quantity*productPrice;
            this.url=cartItem.getProduct().getImageUrl().get(0);
        }
}
