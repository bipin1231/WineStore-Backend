package com.winestore.winestore.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CategoryListDto {
    private List<CategoryUpdateDto> categoryList;
}
