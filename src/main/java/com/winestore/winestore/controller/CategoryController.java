package com.winestore.winestore.controller;

import com.winestore.winestore.DTO.CategoryDTO;
import com.winestore.winestore.DTO.CategoryListDto;
import com.winestore.winestore.DTO.CategoryRequestDTO;
import com.winestore.winestore.DTO.CategoryUpdateDto;
import com.winestore.winestore.entity.Category;
import com.winestore.winestore.repository.CategoryRepo;
import com.winestore.winestore.service.CategoryService;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")

public class CategoryController {


@Autowired
    private CategoryService categoryService;
@Autowired
private CategoryRepo categoryRepo;

@PostMapping
    public String saveProduct(@ModelAttribute CategoryRequestDTO category){
    categoryService.addCategory(category);
    return "hello";
}

    @PutMapping("{name}")
    public String updateCategoory(@RequestParam String name,@RequestBody CategoryRequestDTO category){
        categoryService.updateCategory(name,category);
        return "hello";
    }

    @PutMapping("/update-all")
    public ResponseEntity<?> updateMultipleCategory(@ModelAttribute CategoryListDto dtoList){
    try {
        List<CategoryUpdateDto> dto=dtoList.getCategoryList();
        categoryService.updateMultipleCategory(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>("error"+e,HttpStatus.BAD_REQUEST);
    }
    }

@GetMapping
    public List<CategoryDTO> getCategory(){
    return categoryService.getCategory();
}
@GetMapping("all")
public List<Category> getCategoryAll(){
    return categoryRepo.findAll();
}


@GetMapping("{name}")
public CategoryDTO getCateogryByName(@PathVariable String name){
    return categoryService.getCategoryByName(name);
}
}
