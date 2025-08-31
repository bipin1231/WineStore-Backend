// PlaceOrderResponseDto.java
package com.winestore.winestore.DTO.order;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PlaceOrderResponseDto {
    private Long orderItemId;
    private String productName;   
    private Integer quantity;
    private Integer totalPrice;   // or Double/BigDecimal as per your model
    private LocalDateTime dateTime;
}
