package com.winestore.winestore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double totalPrice;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime orderDate;
    @Column(unique = true, updatable = false)
    private Double deliveryCharge;
    private String orderNumber;
    private String paymentType;
    private String paymentStatus;
    private String orderStatus;
    private String transactionId;
    private String orderStatusFromAdmin;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderItem> orderItem;


}
