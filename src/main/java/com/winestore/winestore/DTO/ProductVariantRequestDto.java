package com.winestore.winestore.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductVariantRequestDto {
    private Integer stock;
    private Double sellingPrice;
    private Double costPrice;
    private Double cartoonCostPrice;
    private Double cartoonSellingPrice;
    private List<MultipartFile> imageUrl;
    private Long sizeId;

}
