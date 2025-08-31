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

//    @Transactional
//    public Order placeCartOrder(Long userId){
//        User user = userRepo.findById(userId).get();
//        Cart cart = cartRepo.findByUserId(user.getId())
//                .orElseThrow(() -> new RuntimeException("Cart not found"));
//
//        List<CartItem> cartItems=cart.getCartItem();
//        if (cartItems.isEmpty()) {
//            throw new RuntimeException("Cart is empty");
//        }
//        Order order=new Order();
//        order.setUser(user);
//
//        List<OrderItem> orderItem=new ArrayList<>();
//        double total=0;
//        for (CartItem cartItem:cart.getCartItem()){
//
//            OrderItem item=new OrderItem();
//
//            item.setProduct(cartItem.getProduct());
//            item.setQuantity(cartItem.getQuantity());
//       //     item.setPrice(cartItem.getProduct().getPrice());
//            item.setOrder(order);
//
//            total+=item.getPrice()*item.getQuantity();
//            orderItem.add(item);
//        }
//
//        order.setTotalPrice(total);
//        order.setOrderItem(orderItem);
//
//        //save order
//
//        orderRepo.save(order);
//
//        //clear cart
//
//        cart.getCartItem().clear();
//        cartRepo.save(cart);
//
//
//return order;
//
//    }

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
                .build();

        // Create Order using Builder
        Order order = Order.builder()
                .user(user)
                .orderItem(List.of(orderItem))
                .totalPrice(productVariant.getSellingPrice() * quantity)
                .paymentType(placeOrder.getPaymentType())
                .build();

        // Set reverse relation
        orderItem.setOrder(order);

        // Save order (cascades to orderItem)
        orderRepo.save(order);



        // Update stock
//        productVariant.setStock(productVariant.getStock() - quantity);
//        productVariantRepo.save(productVariant); // 💡 Save this, not the product

        return new OrderDTO(order);

    }


    public Optional<Order> getCartById(Long id){
        return orderRepo.findByUserId(id);
    }

  //  @PreAuthorize("hasRole('ADMIN')")
    public List<OrderDTO> getAllOrder(){
        return orderRepo.findAll().stream().
                map(OrderDTO::new).
                collect(Collectors.toList());
    }
}
