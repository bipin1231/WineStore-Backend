package com.winestore.winestore.DTO;

import com.winestore.winestore.entity.Category;
import lombok.Data;

import java.util.List;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private List<CategoryDTO> subcategories;
    private String img;

    public CategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.img= category.getImageUrl();
        if (!category.getSubcategories().isEmpty()) {
            this.subcategories = category.getSubcategories().stream().map(CategoryDTO::new).toList();
        }
    }
}
