package com.winestore.winestore.service;

import com.winestore.winestore.DTO.CartDTO;
import com.winestore.winestore.DTO.OrderDTO;
import com.winestore.winestore.entity.*;
import com.winestore.winestore.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Repository
public class OrderService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CartItemRepo cartItemRepo;
    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private OrderItemRepo orderItemRepo;
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductSizeRepo productSizeRepo;


    @Transactional
    public Order placeCartOrder(Long userId){
        User user = userRepo.findById(userId).get();
        Cart cart = cartRepo.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> cartItems=cart.getCartItem();
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        Order order=new Order();
        order.setUser(user);

        List<OrderItem> orderItem=new ArrayList<>();
        double total=0;
        for (CartItem cartItem:cart.getCartItem()){

            OrderItem item=new OrderItem();

            item.setProduct(cartItem.getProduct());
            item.setQuantity(cartItem.getQuantity());
       //     item.setPrice(cartItem.getProduct().getPrice());
            item.setOrder(order);

            total+=item.getPrice()*item.getQuantity();
            orderItem.add(item);
        }

        order.setTotalPrice(total);
        order.setOrderItem(orderItem);

        //save order

        orderRepo.save(order);

        //clear cart

        cart.getCartItem().clear();
        cartRepo.save(cart);


return order;

    }

//    @Transactional
//    public OrderDTO placeOrder(String productName,int quantity,String size) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = null;
//        if (authentication != null && authentication.isAuthenticated()) {
//          username= authentication.getName(); // This will return the username
//        }
//
//        User user = userRepo.findByUsername(username).get();
//        Product product=productRepo.findByName(productName).get();
//
//
//
//        Order order=new Order();
//        OrderItem orderItem=new OrderItem();
//
//
//        order.setUser(user);
//       // order.setTotalPrice(product.getPrice() * quantity);
//
//
//        orderItem.setProduct(product);
//        orderItem.setQuantity(quantity);
//       // orderItem.setPrice(product.getPrice());
//        orderItem.setOrder(order);
//
//        order.setOrderItem(List.of(orderItem));
//
//        orderRepo.save(order);
//
//        orderItemRepo.save(orderItem);
//
//      //  product.setStock(product.getStock()-quantity);
//        productRepo.save(product);

//        return new OrderDTO(order);
  //  }

//    public Optional<Order> getCartById(Long id){
//        return orderRepo.findByUserId(id);
//    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderDTO> getAllOrder(){
        return orderRepo.findAll().stream().map(order->new OrderDTO(order)).collect(Collectors.toList());
    }
}
