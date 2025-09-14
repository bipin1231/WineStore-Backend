package com.winestore.winestore.service;

import com.winestore.winestore.DTO.OrderDTO;
import com.winestore.winestore.DTO.order.PlaceOrderAddDTO;
import com.winestore.winestore.entity.*;
import com.winestore.winestore.mapper.OrderMapper;
import com.winestore.winestore.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
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
    private ProductVariantRepo productVariantRepo;

    @Autowired
    private  OrderMapper orderMapper;

    public String GenerateOrderNumber(){
        // Generate order number using current timestamp
        String datePart = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String timePart = String.valueOf(System.currentTimeMillis()); // unique enough

        return "ORD" + datePart + timePart;
    }

    @Transactional
    public OrderDTO placeCartOrder(Long userId){
        User user = userRepo.findById(userId).orElseThrow(()->new RuntimeException("User Doesn't Exists"));
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
            item.setProductVariant(cartItem.getProductVariant());
            item.setOrder(order);

            total+=cartItem.getTotalPrice();
            orderItem.add(item);

        }

        order.setTotalPrice(total);
        order.setOrderItem(orderItem);
        order.setOrderNumber(GenerateOrderNumber());

        //save order

        orderRepo.save(order);

        //clear cart

        cart.getCartItem().clear();
        cartRepo.save(cart);


return new OrderDTO(order);

    }

    @Transactional
    public OrderDTO placeOrder(PlaceOrderAddDTO placeOrder) {
        User user = userRepo.findById(placeOrder.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepo.findById(placeOrder.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductVariant productVariant = productVariantRepo.findById(placeOrder.getProductVariantId())
                .orElseThrow(() -> new RuntimeException("Product Variant not found"));

        int quantity = placeOrder.getQuantity();



        // Create OrderItem using Builder
        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .productVariant(productVariant)
                .quantity(quantity)
                .totalPrice(productVariant.getSellingPrice()*quantity)
                .build();

        // Generate order number using current timestamp
        String datePart = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String timePart = String.valueOf(System.currentTimeMillis()); // unique enough

        // Create Order using Builder
        Order order = Order.builder()
                .user(user)
                .orderItem(new ArrayList<>(List.of(orderItem)))
                .totalPrice(productVariant.getSellingPrice() * quantity + placeOrder.getDeliveryCharges())
                .paymentType(placeOrder.getPaymentType())
                .orderNumber(GenerateOrderNumber())
                .orderStatus("processing")
                .build();

        // Set reverse relation
        orderItem.setOrder(order);

        // Save order (cascades to orderItem)
     Order savedOrder = orderRepo.save(order);

//        Order newSavedOrder=orderRepo.saveAndFlush(savedOrder);
        return new OrderDTO(savedOrder);
    }

    public OrderDTO updatePaymentStatus(Long id,String paymentType,String paymentStatus){
        Order order= orderRepo.findById(id).orElseThrow(()->new RuntimeException("order not found"));
        order.setPaymentType(paymentType);
        order.setPaymentStatus(paymentStatus);
        Order newOrder=  orderRepo.save(order);
        return new OrderDTO(newOrder);

    }


    public OrderDTO updateOrderStatus(Long id,String orderStatus){
        Order order= orderRepo.findById(id).orElseThrow(()->new RuntimeException("order not found"));
        order.setOrderStatus(orderStatus);
      Order newOrder=  orderRepo.save(order);
        return new OrderDTO(newOrder);

    }

    public List<OrderDTO> getOrderByUserId(Long id){
        return orderRepo.findByUserId(id).stream().
                map(OrderDTO::new).
                collect(Collectors.toList());
    }

  //  @PreAuthorize("hasRole('ADMIN')")
    public List<OrderDTO> getAllOrder(){
        return orderRepo.findAll().stream().
                map(OrderDTO::new).
                collect(Collectors.toList());
    }
}
