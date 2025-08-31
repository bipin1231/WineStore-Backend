package com.winestore.winestore.DTO.order;

import lombok.Data;

@Data
public class PlaceOrderAddDTO {
    private Long userId;
    private Long productId;
    private Long productVariantId;
    private Integer quantity;
    private Integer deliveryCharges;
    private String paymentType;
}
