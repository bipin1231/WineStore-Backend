package com.winestore.winestore.DTO;

import lombok.Data;
@Data
public class PlaceOrderRequestDto {

    private Long userId;
    private Long productId;
    private Long productSizeId;
    private Integer quantity;
}
