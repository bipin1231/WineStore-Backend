package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.Product;
import com.winestore.winestore.entity.ProductSize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor

public class ProductSizeDTO {
    private long id;
    private Integer stock = 0;
    private Double sellingPrice;
    private Double costPrice;
    private String size;

    public ProductSizeDTO(ProductSize productSize){
        this.id= productSize.getId();
        this.stock=productSize.getStock();
        this.sellingPrice=productSize.getSellingPrice();
        this.costPrice=productSize.getCostPrice();
        this.size=productSize.getSize();
    }

}
