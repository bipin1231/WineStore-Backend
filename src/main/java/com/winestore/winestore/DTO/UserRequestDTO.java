package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.User;
import lombok.Data;

@Data
public class UserRequestDTO {

 private String email;

 private String password;

 private String firstName;

 private String lastName;
 private String imageUrl;
}
