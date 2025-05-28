package com.winestore.winestore.controller;

import com.winestore.winestore.DTO.CartDTO;
import com.winestore.winestore.DTO.CartItemAddRequestDTO;
import com.winestore.winestore.DTO.CartItemDTO;
import com.winestore.winestore.entity.Cart;
import com.winestore.winestore.entity.CartItem;
import com.winestore.winestore.entity.Category;
import com.winestore.winestore.service.CartItemService;
import com.winestore.winestore.service.CartService;
import com.winestore.winestore.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody CartItemAddRequestDTO cartItemAddRequestDTO) {
        Cart cart = cartService.createOrUpdateCart(cartItemAddRequestDTO.getUserId());
        cartItemService.addItemToCart(cart,cartItemAddRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCartItem(@RequestParam Long cartItemId,
                                   @RequestParam int quantity){
        //Cart cart=cartService.createOrUpdateCart(username);

        cartItemService.updateCartItem(cartItemId,quantity);


        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<?> removeCartItem(@PathVariable Long cartItemId
                                ){


        cartItemService.removeCartItem(cartItemId);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }
    @GetMapping("/{id}")
    public List<CartItemDTO> getCartById(@PathVariable Long id){
        return cartService.getCartItemByUserId(id);
    }
    
    @GetMapping
    public List<CartDTO> getAllCart(){
        return cartService.getAllCart();
    }



}
