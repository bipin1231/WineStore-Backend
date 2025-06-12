package com.winestore.winestore.DTO;

import lombok.Data;

@Data
public class ProductUpdateDto {
    private Long id;
    private String name;
    private String description;
    private String categoryName;
}
