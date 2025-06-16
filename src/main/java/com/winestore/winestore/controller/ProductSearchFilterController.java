package com.winestore.winestore.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winestore.winestore.DTO.*;
import com.winestore.winestore.entity.Product;
import com.winestore.winestore.repository.ProductRepo;
import com.winestore.winestore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product")

public class ProductSearchFilterController {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductService productService;

    @GetMapping("/search-list")
    public List<String> getMatchingNamesBySearchQuery(@RequestParam String query){
        return productService.getMatchingNamesBySearchQuery(query);
    }

    @GetMapping("/search")
    public List<ProductSizeWithProductDTO> getProductByQuery(@RequestParam String query){
    return productService.findByQuery(query);
    }

    @GetMapping("/filter")
    public List<ProductResponseDTO> getProductByQueryFilter(@RequestParam String query){
        return productService.filterByQuery(query);
    }


    @GetMapping("filter-and-sort")
    public Page<ProductSizeWithProductDTO> filterAndSort(@RequestParam(required = false) String categoryName,
                                                         @RequestParam(required = false) Double minPrice,
                                                         @RequestParam(required = false) Double maxPrice,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "20") int size,
                                                         @RequestParam(defaultValue = "asc") String sort){
        return productService.filterAndSort(categoryName,minPrice,maxPrice,page,size,sort);
    }
}
