package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.ProductVariant;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class ProductSizeDTO {
    private long id;
    private Integer stock;
    private Double sellingPrice;
    private Double costPrice;
    private String size;

    public ProductSizeDTO(ProductVariant productVariant){
        this.id= productVariant.getId();
        this.stock= productVariant.getStock();
        this.sellingPrice= productVariant.getSellingPrice();
        this.costPrice= productVariant.getCostPrice();
      //  this.size= productVariant.getSize();
    }

}
