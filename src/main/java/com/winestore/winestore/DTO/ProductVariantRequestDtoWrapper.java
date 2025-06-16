package com.winestore.winestore.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductVariantRequestDtoWrapper {
private List<ProductVariantRequestDto> productVariantRequestDto;

}
