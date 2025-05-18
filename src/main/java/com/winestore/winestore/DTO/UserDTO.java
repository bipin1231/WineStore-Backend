package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.User;
import lombok.Data;

@Data
public class UserDTO {
 private Long id;
 private String username;
 private String email;
 private String role;

 public UserDTO(User user){
     this.id=user.getId();
     this.username=user.getUsername();
     this.email=user.getEmail();
     this.role=user.getRoles();
 }

}
