package com.winestore.winestore.service;

import com.winestore.winestore.DTO.OrderDTO;
import com.winestore.winestore.DTO.PlaceOrderRequestDto;
import com.winestore.winestore.entity.*;
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
    private ProductSizeRepo productSizeRepo;


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
    public void placeOrder(PlaceOrderRequestDto placeOrder) {
        User user = userRepo.findById(placeOrder.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepo.findById(placeOrder.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductSize productSize = productSizeRepo.findById(placeOrder.getProductSizeId())
                .orElseThrow(() -> new RuntimeException("Product size not found"));

        int quantity = placeOrder.getQuantity();

        if (productSize.getStock() < quantity) {
            throw new RuntimeException("Not enough stock");
        }

        // Create order
        Order order = new Order();
        order.setUser(user);

        // Create order item
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setSize(productSize);
        orderItem.setOrder(order);

        order.setOrderItem(List.of(orderItem)); // Assuming it's a one-to-many relationship
        order.setTotalPrice(productSize.getSellingPrice() * quantity);

        // Save order (cascades to orderItem if configured)
        orderRepo.save(order);
        orderItemRepo.save(orderItem);

        // Update stock
        productSize.setStock(productSize.getStock() - quantity);
        productSizeRepo.save(productSize); // 💡 Save this, not the product

        new OrderDTO(order);
    }


    public Optional<Order> getCartById(Long id){
        return orderRepo.findByUserId(id);
    }

  //  @PreAuthorize("hasRole('ADMIN')")
    public List<OrderDTO> getAllOrder(){
        return orderRepo.findAll().stream().map(order->new OrderDTO(order)).collect(Collectors.toList());
    }
}
