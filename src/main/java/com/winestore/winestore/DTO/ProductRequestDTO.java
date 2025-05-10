package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.ProductSize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductRequestDTO {
    private String name;
    private double cartoonPrice;
    private String description;
    private String category;

    private List<ProductSizeDTO> productSize;


}
