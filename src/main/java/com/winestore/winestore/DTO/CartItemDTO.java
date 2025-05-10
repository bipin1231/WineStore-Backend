package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.CartItem;

public class CartItemDTO {
        private Long cartItemId;
        private String productName;
        private int quantity;
        private double price;

        public CartItemDTO(CartItem cartItem){
            this.cartItemId=cartItem.getId();
            this.productName=cartItem.getProduct().getName();
            this.quantity=cartItem.getQuantity();
         //   this.price=cartItem.getProduct().getPrice();
        }
    public Long getProductId() {
        return cartItemId;
    }

    public void setProductId(Long cartItemIdId) {
        this.cartItemId = cartItemIdId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
