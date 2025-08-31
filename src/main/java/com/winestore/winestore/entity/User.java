package com.winestore.winestore.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.winestore.winestore.AuthProvider;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"email", "authProvider"})
        }
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
     private String email;
     private String password;
     private String roles="user";
     private String authProvider;

    //  Automatically set when a new user is created
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    //  Automatically update when user record is updated
    @UpdateTimestamp
    private LocalDateTime updatedAt;

//    @Enumerated(EnumType.STRING)
//    private AuthProvider authProvider;

     @OneToOne(mappedBy = "user")
     private Cart cart;

     @OneToMany(mappedBy = "user")
     private List<Order> order;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserDeliveryInfo userDeliveryInfo;

}
