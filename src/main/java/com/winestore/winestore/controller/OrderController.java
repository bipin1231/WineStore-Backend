package com.winestore.winestore.controller;

import com.winestore.winestore.ApiResponse.ApiResponse;
import com.winestore.winestore.DTO.OrderDTO;
import com.winestore.winestore.DTO.order.PlaceOrderAddDTO;
import com.winestore.winestore.service.CartItemService;
import com.winestore.winestore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<OrderDTO>> placeOrder(@RequestBody PlaceOrderAddDTO dto){


            OrderDTO saved=orderService.placeOrder(dto);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Delivery info saved successfully", saved)
        );
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
@GetMapping("/{id}")
public ResponseEntity<ApiResponse<?>> getOrderById(@PathVariable Long id){
   List<OrderDTO> data= orderService.getOrderByUserId(id);
    return ResponseEntity.ok(
            new ApiResponse<>(true, "Order Fetched Successfully", data)
    );

}
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getOrder(){
        List<OrderDTO> data= orderService.getAllOrder();
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Order Fetched Successfully", data)
        );

    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateOrderStatus(@PathVariable Long id,
                                                             @RequestParam String orderStatus
                                                            ){
        OrderDTO data= orderService.updateOrderStatus(id,orderStatus);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Order Updated Successfully", data)
        );

    }



}
