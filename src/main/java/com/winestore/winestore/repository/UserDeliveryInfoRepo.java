package com.winestore.winestore.repository;

import com.winestore.winestore.entity.UserDeliveryInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDeliveryInfoRepo extends JpaRepository<UserDeliveryInfo, Long> {
    Optional<UserDeliveryInfo> findByUserId(Long userId);
}
