package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.OrderItem;
import lombok.Data;

@Data
public class OrderItemDTO {

    private Long productId;
    private String productName;
    private int quantity;
    private Long productVariantId;
    private Double price;


    public OrderItemDTO(OrderItem orderItem) {
        this.productId = orderItem.getProduct().getId();
        this.productName = orderItem.getProduct().getName();
        this.quantity = orderItem.getQuantity();
        this.productVariantId=orderItem.getProductVariant().getId();
        this.price=orderItem.getProductVariant().getSellingPrice();
    }

}
