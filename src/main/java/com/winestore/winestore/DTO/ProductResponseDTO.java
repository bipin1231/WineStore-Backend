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
private double cartoonPrice;
    private List<ProductSizeDTO> productSize;
    private List<String> imageUrl;


    public ProductResponseDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.cartoonPrice=product.getCartoonPrice();
        this.imageUrl=product.getImageUrl();
        this.productSize = product.getProductVariant().stream().map(ProductSizeDTO::new).toList();


        if (product.getCategory() != null) {
            this.categoryName = product.getCategory().getName();
        } else {
            this.categoryName = "No Category";
        }
    }

}
