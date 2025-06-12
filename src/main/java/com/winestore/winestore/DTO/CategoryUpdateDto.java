package com.winestore.winestore.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class CategoryUpdateDto {
    private Long id;
    private String name;
    private String description;
    private MultipartFile image;
}
