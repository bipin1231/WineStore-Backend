package com.winestore.winestore.repository;

import com.winestore.winestore.entity.Product;
import com.winestore.winestore.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmailAndAuthProvider(String email, String authProvider);
}
