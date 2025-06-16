package com.winestore.winestore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name = "product_variant")
@Getter
@Setter
@NoArgsConstructor
public class ProductVariant {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 private int stock = 0;

 private Double sellingPrice;
 private Double costPrice;
private Double cartoonCostPrice;
 private Double cartoonSellingPrice;
 @ElementCollection
 private List<String> imageUrl;


 @ManyToOne
 @JoinColumn(name = "product_id")
 private Product product;

 @ManyToOne
 @JoinColumn(name = "size_id")
 private Size size;

 @OneToMany(mappedBy = "productVariant")
 private List<CartItem> cartItem;

 @OneToMany(mappedBy = "productVariant")
 private List<OrderItem> orderItem;


}