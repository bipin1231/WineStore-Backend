package com.winestore.winestore.controller;

import com.winestore.winestore.DTO.delivery.DeliveryInfoAddDTO;
import com.winestore.winestore.DTO.delivery.DeliveryInfoResponseDTO;
import com.winestore.winestore.entity.UserDeliveryInfo;
import com.winestore.winestore.ApiResponse.ApiResponse;
import com.winestore.winestore.service.UserDeliveryInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/delivery")
public class UserDeliveryInfoController {

    private final UserDeliveryInfoService deliveryInfoService;

    public UserDeliveryInfoController(UserDeliveryInfoService deliveryInfoService) {
        this.deliveryInfoService = deliveryInfoService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse<DeliveryInfoResponseDTO>> addDeliveryInfo(
            @PathVariable Long userId,
            @RequestBody DeliveryInfoAddDTO deliveryInfo) {

        DeliveryInfoResponseDTO saved = deliveryInfoService.saveDeliveryInfo(userId, deliveryInfo);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Delivery info saved successfully", saved)
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<DeliveryInfoResponseDTO>> getDeliveryInfo(@PathVariable Long userId) {
        DeliveryInfoResponseDTO info = deliveryInfoService.getDeliveryInfo(userId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Delivery info fetched successfully", info)
        );
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<DeliveryInfoResponseDTO>> updateDeliveryInfo(
            @PathVariable Long userId,
            @RequestBody DeliveryInfoAddDTO updatedInfo) {

        DeliveryInfoResponseDTO updated = deliveryInfoService.updateDeliveryInfo(userId, updatedInfo);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Delivery info updated successfully", updated)
        );
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteDeliveryInfo(@PathVariable Long userId) {
        deliveryInfoService.deleteDeliveryInfo(userId);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Delivery info deleted successfully", null)
        );
    }
}
