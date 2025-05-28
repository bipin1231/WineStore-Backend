package com.winestore.winestore.controller;

import com.winestore.winestore.DTO.CartDTO;
import com.winestore.winestore.DTO.OrderDTO;
import com.winestore.winestore.entity.Cart;
import com.winestore.winestore.entity.Order;
import com.winestore.winestore.service.CartItemService;
import com.winestore.winestore.service.CartService;
import com.winestore.winestore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartItemService cartItemService;

    @PostMapping("/place-directly")
    public void placeOrder(
                          @RequestParam String productName,
                          @RequestParam int quantity,
                          @RequestParam String size){
       // Cart cart=orderService.createOrUpdateCart(username);

//        orderService.placeOrder(productName,quantity,size);

    }
    @PostMapping("/place-cart-order")
    public void placeCartOrder(@RequestParam Long userId){

       //Order order= orderService.placeCartOrder(userId);
      //  return new OrderDTO(order);
    }
//
//    @DeleteMapping("/delete")
//    public String removeCartItem(@RequestParam String username,
//                                 @RequestParam String productName
//                                ){
//
//
//        cartItemService.removeCartItem(username,productName);
//
//        return "cart item deleted successfully";
//
//    }
    @GetMapping
    public List<OrderDTO> getOrder(){
        return orderService.getAllOrder();
    }



}
