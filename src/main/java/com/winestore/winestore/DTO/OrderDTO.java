package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDTO {
    private Long id;
    private double totalPrice;
    private LocalDateTime orderDate;
    private long userId;
    private List<OrderItemDTO> orderItem;

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.totalPrice= order.getTotalPrice();;
        this.orderDate=order.getOrderDate();
        this.userId=order.getUser().getId();
        this.orderItem = order.getOrderItem()
                .stream()
                .map(OrderItemDTO::new)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public List<OrderItemDTO> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(List<OrderItemDTO> orderItem) {
        this.orderItem = orderItem;
    }
}
