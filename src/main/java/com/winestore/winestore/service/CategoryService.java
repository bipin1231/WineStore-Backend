package com.winestore.winestore.service;

import com.winestore.winestore.DTO.CategoryDTO;
import com.winestore.winestore.DTO.CategoryRequestDTO;
import com.winestore.winestore.entity.Category;
import com.winestore.winestore.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;

    public void addCategory(CategoryRequestDTO categoryRequestDTO){
        if(categoryRequestDTO.getSubcategory()!=null){
            Category parentCategory= categoryRepo.findByName(categoryRequestDTO.getCategory()).orElseThrow(()->new IllegalArgumentException("Invalid category ID"));

            Category subCategory=new Category();
            subCategory.setParent(parentCategory);
            subCategory.setName(categoryRequestDTO.getSubcategory());
            subCategory.setDescription(categoryRequestDTO.getDescription());
            categoryRepo.save(subCategory);
        }else{
            Category category=new Category();
            category.setName(categoryRequestDTO.getCategory());
            category.setDescription(categoryRequestDTO.getDescription());
            category.setParent(null);
            categoryRepo.save(category);
        }

    }
    public void updateCategory(String name,CategoryRequestDTO categoryRequestDTO){
        Category category=categoryRepo.findByName(name).orElseThrow(()->new IllegalArgumentException("category not found"));
        if(categoryRequestDTO.getCategory()!=null) category.setName(categoryRequestDTO.getCategory());
        if (categoryRequestDTO.getDescription()!=null) category.setDescription(categoryRequestDTO.getDescription());
        categoryRepo.save(category);
    }

    public List<CategoryDTO> getCategory(){
        return categoryRepo.findAll()
                .stream()
                .map(CategoryDTO::new)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryByName(String name){
        Optional<Category> category = categoryRepo.findByName(name);
        return new CategoryDTO(category.get());

    }

}
