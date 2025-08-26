package com.winestore.winestore.mapper;

import com.winestore.winestore.DTO.delivery.DeliveryInfoAddDTO;
import com.winestore.winestore.DTO.delivery.DeliveryInfoResponseDTO;
import com.winestore.winestore.entity.UserDeliveryInfo;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface DeliveryInfoMapper {
    DeliveryInfoResponseDTO toResponseDTO(UserDeliveryInfo entity);

    UserDeliveryInfo toEntity(DeliveryInfoAddDTO addDto);

}
