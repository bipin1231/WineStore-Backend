package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.ProductVariant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor

public class ProductVariantUpdateDto {
    private Long id;
    private Integer stock;
    private Double sellingPrice;
    private Double costPrice;
    private Double cartoonCostPrice;
    private Double cartoonSellingPrice;
    private Long sizeId;
}
