package com.winestore.winestore.service;

import com.winestore.winestore.DTO.UserResponseDTO;
import com.winestore.winestore.entity.User;
import com.winestore.winestore.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    private static final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
    public void addUser(User user){

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    public Optional<User> findByEmailAndAuthProvider(String email, String authProvider){
       return userRepo.findByEmailAndAuthProvider(email, authProvider);
    }
    public List<UserResponseDTO> getUser(){
        return userRepo.findAll().stream().map(UserResponseDTO::new).collect(Collectors.toList());
    }
}
