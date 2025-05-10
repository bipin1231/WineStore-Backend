package com.winestore.winestore.controller;

import com.winestore.winestore.DTO.CategoryDTO;
import com.winestore.winestore.DTO.CategoryRequestDTO;
import com.winestore.winestore.entity.Category;
import com.winestore.winestore.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")

public class CategoryController {


@Autowired
    private CategoryService categoryService;

@PostMapping
    public String saveProduct(@RequestBody CategoryRequestDTO category){
    categoryService.addCategory(category);
    return "hello";
}

    @PutMapping("{name}")
    public String updateCategoory(@RequestParam String name,@RequestBody CategoryRequestDTO category){
        categoryService.updateCategory(name,category);
        return "hello";
    }

@GetMapping
    public List<CategoryDTO> getCategory(){
    return categoryService.getCategory().stream().filter(dto->dto.getSubcategories()==null).toList();
}


@GetMapping("{name}")
public CategoryDTO getCateogryByName(@PathVariable String name){
    return categoryService.getCategoryByName(name);
}
}
