package com.winestore.winestore.entity;

import com.winestore.winestore.entity.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_delivery_info")
@Data
public class UserDeliveryInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String address;
    private String mobileNo; //  better as String, not Integer

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true) // ✅ FK here
    private User user;
}
