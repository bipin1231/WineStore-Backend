package com.winestore.winestore.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemAddRequestDTO {
    Long userId;
    Long productId;
    Integer quantity;
    Long productVariantId;


}
