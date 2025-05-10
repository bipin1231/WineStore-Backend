package com.winestore.winestore.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "product_size")
@Getter
@Setter
@NoArgsConstructor
public class ProductSize {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long Id;

 private int stock = 0;
 private double sellingPrice;
 private double costPrice;
 private String size;

 @ManyToOne
 @JoinColumn(name = "product_id")
 private Product product;
}