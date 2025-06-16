package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.ProductVariant;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductVariantResponseDto {
    private Long id;
    private Integer stock;
    private Double sellingPrice;
    private Double costPrice;
    private Double cartoonCostPrice;
    private Double cartoonSellingPrice;
    private List<String> imageUrl;
    private Long sizeId;

    public ProductVariantResponseDto(ProductVariant productVariant){
        this.id=productVariant.getId();
        this.stock= productVariant.getStock();
        this.sellingPrice=productVariant.getSellingPrice();
        this.costPrice=productVariant.getCostPrice();
        this.sizeId=productVariant.getSize().getId();
        this.cartoonCostPrice=productVariant.getCartoonCostPrice();
        this.cartoonSellingPrice=productVariant.getCartoonSellingPrice();
        this.imageUrl=productVariant.getImageUrl();
    }
}
