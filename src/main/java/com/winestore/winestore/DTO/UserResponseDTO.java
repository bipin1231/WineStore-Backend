package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.User;
import lombok.Data;

@Data
public class UserResponseDTO {
 private Long id;
private String firstName;
private String lastName;
private String imageUrl;
 private String email;
 private String role;

 public UserResponseDTO(User user){
     this.id=user.getId();
    this.firstName= user.getFirstName();
    this.lastName= user.getLastName();
     this.email=user.getEmail();
     this.role=user.getRoles();
     this.imageUrl=user.getImageUrl();
 }

}
