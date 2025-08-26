package com.winestore.winestore.DTO.delivery;

import lombok.Data;

@Data
public class DeliveryInfoResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String mobileNo;

}
