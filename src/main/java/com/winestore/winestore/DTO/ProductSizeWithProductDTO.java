package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.ProductVariant;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor

public class ProductSizeWithProductDTO {
    private Long productVariantId;
    private Long productId;
    private String productName;
    private Integer stock;
    private Double price;
    private Long productSizeId;
    private String size;
    private String categoryName;
    private String imageUrl;

    public ProductSizeWithProductDTO(ProductVariant productVariant){
        this.productVariantId= productVariant.getId();
        this.stock= productVariant.getStock();
        this.price= productVariant.getSellingPrice();
        this.productSizeId=productVariant.getSize().getId();
        this.size= productVariant.getSize().getSize();
        this.productId= productVariant.getProduct().getId();
        this.productName= productVariant.getProduct().getName();
        this.categoryName= productVariant.getProduct().getCategory().getName();
        List<String> images = productVariant.getImageUrl();
        this.imageUrl = (images != null && !images.isEmpty()) ? images.get(0) : null;

    }


}
