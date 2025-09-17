package com.winestore.winestore.controller;

import com.winestore.winestore.ApiResponse.ApiResponse;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")

public class CategoryController {


@Autowired
    private CategoryService categoryService;
@Autowired
private CategoryRepo categoryRepo;


@PreAuthorize("hasRole('ADMIN')")
@PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse<?>>  saveProduct(@ModelAttribute CategoryRequestDTO category){
    categoryService.addCategory(category);
    return ResponseEntity.ok(
            new ApiResponse<>(true,"Category Added Successfully",null)
    );

}

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{name}")
    public String updateCategoory(@RequestParam String name,@RequestBody CategoryRequestDTO category){
        categoryService.updateCategory(name,category);
        return "hello";
    }

    @PreAuthorize("hasRole('ADMIN')")
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

@PreAuthorize("hasRole('ADMIN')")
@DeleteMapping("{id}")
    public String deleteCategory(@PathVariable Long id){
    categoryService.deleteCategory(id);
    return "category deleted successfully";
}
//    @GetMapping("/auth-debug")
//    public Object debugAuth() {
//        var auth = SecurityContextHolder.getContext().getAuthentication();
//
//        String principal;
//        if (auth.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
//            principal = userDetails.getUsername();
//        } else {
//            principal = auth.getPrincipal().toString();
//        }
//
//        return Map.of(
//                "principal", principal,
//                "authorities", auth.getAuthorities().toString()
//
//        );
//    }

}
