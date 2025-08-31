package com.winestore.winestore.mapper;

import com.winestore.winestore.DTO.OrderDTO;
import com.winestore.winestore.DTO.OrderItemDTO;
import com.winestore.winestore.entity.Order;
import com.winestore.winestore.entity.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDTO toOrderDTO(Order order);

    OrderItemDTO toOrderItemDTO(OrderItem orderItem);
}
