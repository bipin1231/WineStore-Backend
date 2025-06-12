package com.winestore.winestore.service;

import com.winestore.winestore.DTO.CategoryDTO;
import com.winestore.winestore.DTO.CategoryRequestDTO;
import com.winestore.winestore.DTO.CategoryUpdateDto;
import com.winestore.winestore.entity.Category;
import com.winestore.winestore.repository.CategoryRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ImageService imageService;

    public void addCategory(CategoryRequestDTO categoryRequestDTO){

        try {
            if(categoryRepo.findByName(categoryRequestDTO.getCategory()).isPresent()){
                throw new IllegalArgumentException("category with this name already exits");
            }

            if (categoryRequestDTO.getParentCategory() != null &&
                    !categoryRequestDTO.getParentCategory().equalsIgnoreCase("null")) {
                System.out.println("parentCategory: " + categoryRequestDTO.getParentCategory());

                Category parentCategory = categoryRepo.findByName(categoryRequestDTO.getParentCategory()).orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
                String imageUrl=imageService.addImage(categoryRequestDTO.getImage());
                Category subCategory = new Category();
                subCategory.setParent(parentCategory);
                subCategory.setName(categoryRequestDTO.getCategory());
                subCategory.setDescription(categoryRequestDTO.getDescription());
                subCategory.setImageUrl(imageUrl);
                categoryRepo.save(subCategory);
            } else {
                Category category = new Category();
                String imageUrl=imageService.addImage(categoryRequestDTO.getImage());
                category.setName(categoryRequestDTO.getCategory());
                category.setDescription(categoryRequestDTO.getDescription());
                category.setParent(null);
                category.setImageUrl(imageUrl);
                categoryRepo.save(category);
            }
        }catch (Exception e) {
            System.out.println("errrorr"+e);
        }

    }
    public void updateCategory(String name,CategoryRequestDTO categoryRequestDTO){
        Category category=categoryRepo.findByName(name).orElseThrow(()->new IllegalArgumentException("category not found"));
        if(categoryRequestDTO.getCategory()!=null) category.setName(categoryRequestDTO.getCategory());
        if (categoryRequestDTO.getDescription()!=null) category.setDescription(categoryRequestDTO.getDescription());
        categoryRepo.save(category);
    }

    @Transactional
    public void updateMultipleCategory(List<CategoryUpdateDto> dto) throws IOException {

        //        dto
//                .forEach(cat -> {
//            Category category=categoryRepo.findById(cat.getId()).orElseThrow(()->new IllegalArgumentException("category not found"));
//            if(cat.getName()!=null) category.setName(cat.getName());
//            if(cat.getDescription()!=null) category.setDescription(cat.getDescription());
//        });

        if (dto == null || dto.isEmpty()) {
            throw new IllegalArgumentException("Category update list cannot be null or empty");
        }
        for (CategoryUpdateDto cat:dto){
            Category category=categoryRepo.findById(cat.getId()).orElseThrow(()->new IllegalArgumentException("category not found"));

            if(cat.getName()!=null) category.setName(cat.getName());
            if(cat.getDescription()!=null) category.setDescription(cat.getDescription());
            if (cat.getImage() != null && !cat.getImage().isEmpty()) {
                String imageUrl = imageService.addImage(cat.getImage());
                category.setImageUrl(imageUrl); // Assuming category has `img` field
            }
        }
    }

    public List<CategoryDTO> getCategory(){
        return categoryRepo.findByParentIsNull()
                .stream()
                .map(CategoryDTO::new)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryByName(String name){
        Optional<Category> category = categoryRepo.findByName(name);
        return new CategoryDTO(category.get());

    }

}
