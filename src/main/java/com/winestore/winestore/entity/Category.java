package com.winestore.winestore.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
@Data
@NoArgsConstructor
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NonNull
    private String name;

    @Column(nullable = true)
    private String description;

    // Parent Category
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;


    // Subcategories
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Category> subcategories;



    @OneToMany(mappedBy = "category",fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Product> products;

}
