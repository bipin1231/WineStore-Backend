package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.Category;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
public class CategoryRequestDTO {
    private String category;
    private String description;
    private String parentCategory;
    private MultipartFile image;

}
