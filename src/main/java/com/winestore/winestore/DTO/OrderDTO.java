package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.Order;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Data
public class OrderDTO {
    private Long id;
    private double totalPrice;
    private LocalDateTime orderDate;
    private long userId;
    private String orderNumber;
    private List<OrderItemDTO> orderItem;
    private String paymentType;
    private String paymentStatus;
    private String orderStatus;

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.totalPrice= order.getTotalPrice();;
        this.orderDate=order.getOrderDate();
        this.orderNumber=order.getOrderNumber();
        this.userId=order.getUser().getId();
        this.paymentStatus=order.getPaymentStatus();
        this.paymentType=order.getPaymentType();
        this.orderStatus=order.getOrderStatus();
        this.orderItem = order.getOrderItem()
                .stream()
                .map(OrderItemDTO::new)
                .collect(Collectors.toList());
    }
}
