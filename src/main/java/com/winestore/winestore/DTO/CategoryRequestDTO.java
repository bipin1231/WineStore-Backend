package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CategoryRequestDTO {
    private String category;
    private String description;
    private String subcategory;

}
