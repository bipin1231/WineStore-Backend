package com.winestore.winestore.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductRequestDTO {
    private String name;
    private String description;
    private Long categoryId;
    private List<ProductVariantRequestDto> productVariantRequestDto;
}
