package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.ProductVariant;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class ProductSizeWithProductDTO {
    private Long productSizeId;
    private Long productId;
    private String productName;
    private Integer stock;
    private Double price;
    private String size;
    private String categoryName;

    public ProductSizeWithProductDTO(ProductVariant productVariant){
        this.productSizeId= productVariant.getId();
        this.stock= productVariant.getStock();
        this.price= productVariant.getSellingPrice();
        this.size= productVariant.getSize().getSize();
        this.productId= productVariant.getProduct().getId();
        this.productName= productVariant.getProduct().getName();
        this.categoryName= productVariant.getProduct().getCategory().getName();

    }


}
