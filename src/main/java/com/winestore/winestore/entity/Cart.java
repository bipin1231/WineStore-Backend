package com.winestore.winestore.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

     @OneToMany(mappedBy = "cart",cascade=CascadeType.ALL,orphanRemoval = true)
    private List<CartItem> cartItem;

    private int quantity=0;
    private double totalPrice;
}
