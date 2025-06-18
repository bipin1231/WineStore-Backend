package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String categoryName;
    private List<ProductVariantResponseDto> productVariant;



    public ProductResponseDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.productVariant = product.getProductVariant().stream().map(ProductVariantResponseDto::new).toList();


        if (product.getCategory() != null) {
            this.categoryName = product.getCategory().getName();
        } else {
            this.categoryName = "No Category";
        }
    }

}
