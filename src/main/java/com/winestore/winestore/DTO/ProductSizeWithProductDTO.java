package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.ProductSize;
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

    public ProductSizeWithProductDTO(ProductSize productSize){
        this.productSizeId= productSize.getId();
        this.stock=productSize.getStock();
        this.price=productSize.getSellingPrice();
        this.size=productSize.getSize();
        this.productId=productSize.getProduct().getId();
        this.productName=productSize.getProduct().getName();
        this.categoryName=productSize.getProduct().getCategory().getName();

    }

}
