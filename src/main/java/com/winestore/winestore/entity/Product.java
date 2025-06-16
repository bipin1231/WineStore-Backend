package com.winestore.winestore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
public class Product {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long Id;
 private String name;

 private double cartoonPrice=0;

 @Column(nullable = true)
 private String description;


@ElementCollection
 private List<String> imageUrl;


 @OneToMany(mappedBy = "product")
 private List<CartItem> cartItem;

 @OneToMany(mappedBy = "product")
 private List<OrderItem> orderItems;


 @ManyToOne
 @JoinColumn(name = "category_id")
 private Category category;

 @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
 private List<ProductVariant> productVariant;
}



