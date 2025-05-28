package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.User;
import lombok.Data;

@Data
public class UserResponseDTO {
 private Long id;

 private String email;
 private String role;

 public UserResponseDTO(User user){
     this.id=user.getId();

     this.email=user.getEmail();
     this.role=user.getRoles();
 }

}
