package com.winestore.winestore.service;

import com.winestore.winestore.entity.User;
import com.winestore.winestore.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //Long id= Long.parseLong(userId);
        Optional<User> user = userRepo.findByEmailAndAuthProvider(email,"none");
        if (user.isPresent()) {
            return org.springframework.security.core.userdetails.User.builder()

                    .username(user.get().getEmail())
                    .password(user.get().getPassword())
                    .roles(user.get().getRoles())
                    .build();
        }
        throw new UsernameNotFoundException("User not found");
    }
}
