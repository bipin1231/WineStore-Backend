package com.winestore.winestore.service;

import com.winestore.winestore.DTO.delivery.DeliveryInfoAddDTO;
import com.winestore.winestore.DTO.delivery.DeliveryInfoResponseDTO;
import com.winestore.winestore.entity.User;
import com.winestore.winestore.entity.UserDeliveryInfo;
import com.winestore.winestore.mapper.DeliveryInfoMapper;
import com.winestore.winestore.repository.UserDeliveryInfoRepo;
import com.winestore.winestore.repository.UserRepo;

import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserDeliveryInfoService {

    private final UserDeliveryInfoRepo deliveryInfoRepository;
    private final UserRepo userRepository;
    private final DeliveryInfoMapper mapper;

    public UserDeliveryInfoService(UserDeliveryInfoRepo deliveryInfoRepository, UserRepo userRepository,
                                   DeliveryInfoMapper mapper) {
        this.deliveryInfoRepository = deliveryInfoRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    // Save delivery info
    public DeliveryInfoResponseDTO saveDeliveryInfo(Long userId, DeliveryInfoAddDTO deliveryInfoDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Map DTO -> Entity
        UserDeliveryInfo userDeliveryInfo = mapper.toEntity(deliveryInfoDTO);
        userDeliveryInfo.setUser(user);

        UserDeliveryInfo saved = deliveryInfoRepository.save(userDeliveryInfo);

        // Map Entity -> Response DTO
        return mapper.toResponseDTO(saved);
    }

    // Get delivery info
    public DeliveryInfoResponseDTO getDeliveryInfo(Long userId) {
        UserDeliveryInfo deliveryInfo = deliveryInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Delivery info not found"));

        return mapper.toResponseDTO(deliveryInfo);
    }

    // Update delivery info
    public DeliveryInfoResponseDTO updateDeliveryInfo(Long userId, DeliveryInfoAddDTO updatedDTO) {
        UserDeliveryInfo deliveryInfo = deliveryInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Delivery info not found"));

        // Map updated DTO -> existing entity
        deliveryInfo.setFirstName(updatedDTO.getFirstName());
        deliveryInfo.setLastName(updatedDTO.getLastName());
        deliveryInfo.setAddress(updatedDTO.getAddress());
        deliveryInfo.setMobileNo(updatedDTO.getMobileNo());

        UserDeliveryInfo updated = deliveryInfoRepository.save(deliveryInfo);
        return mapper.toResponseDTO(updated);
    }

    // Delete
    public void deleteDeliveryInfo(Long userId) {
        UserDeliveryInfo deliveryInfo = deliveryInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Delivery info not found"));
        deliveryInfoRepository.delete(deliveryInfo);
    }
}
