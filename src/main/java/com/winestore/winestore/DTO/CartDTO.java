package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.Cart;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
@Data
public class CartDTO {
    private Long id;
    private Long userId;
    private List<CartItemDTO> cartItem;

public CartDTO(Cart cart){
    this.id=cart.getId();
    this.userId=cart.getUser().getId();
//    this.cartItem=cart.getCartItem()
//                        .stream()
//                        .map(CartItemDTO::new)
//                        .collect(Collectors.toList());
}




}
