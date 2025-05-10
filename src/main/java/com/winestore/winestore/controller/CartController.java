package com.winestore.winestore.controller;

import com.winestore.winestore.DTO.CartDTO;
import com.winestore.winestore.entity.Cart;
import com.winestore.winestore.entity.CartItem;
import com.winestore.winestore.entity.Category;
import com.winestore.winestore.service.CartItemService;
import com.winestore.winestore.service.CartService;
import com.winestore.winestore.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @PostMapping("/add")
    public void addToCart(@RequestParam String username,
                          @RequestParam String productName,
                          @RequestParam int quantity){
        Cart cart=cartService.createOrUpdateCart(username);

        cartItemService.addItem(cart,productName,quantity);

    }
    @PutMapping("/update")
    public String updateCartItem(@RequestParam String username,
                                   @RequestParam String productName,
                                   @RequestParam int quantity){
        //Cart cart=cartService.createOrUpdateCart(username);

        cartItemService.updateCartItem(username,productName,quantity);

        return "cart item updated successfully";

    }

    @DeleteMapping("/delete")
    public String removeCartItem(@RequestParam String username,
                                 @RequestParam String productName
                                ){


        cartItemService.removeCartItem(username,productName);

        return "cart item deleted successfully";

    }
    @GetMapping
    public List<CartDTO> getCart(){
        return cartService.getAllCart();
    }



}
