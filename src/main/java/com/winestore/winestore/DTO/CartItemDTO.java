package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.CartItem;
import lombok.Data;

@Data
public class CartItemDTO {
        private Long cartItemId;
    private Long productVariantId;
        private String name;
        private int quantity;
        private double totalPrice;
    private double sellingPrice;
        private String size;
        private String imageUrl;
        public CartItemDTO(CartItem cartItem) {
            this.cartItemId = cartItem.getId();
            this.name = cartItem.getProduct().getName();
            this.quantity = cartItem.getQuantity();
            this.size=cartItem.getProductVariant().getSize().getSize();
            this.sellingPrice =cartItem.getProductVariant().getSellingPrice();
            this.totalPrice=quantity* sellingPrice;
            this.imageUrl=cartItem.getProductVariant().getImageUrl().get(0);
            this.productVariantId=cartItem.getProductVariant().getId();
        }
}
