package com.winestore.winestore.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.winestore.winestore.AuthProvider;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"email", "authProvider"})
        }
)
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
     private String email;
     private String password;
     private String roles="user";

     private String authProvider;

//    @Enumerated(EnumType.STRING)
//    private AuthProvider authProvider;

     @OneToOne(mappedBy = "user")
     private Cart cart;

     @OneToMany(mappedBy = "user")
     private List<Order> order;

}
