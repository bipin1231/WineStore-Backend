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
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/product")

public class ProductController {

    @Autowired
        private ProductRepo productRepo;

    @Autowired
        private ProductService productService;


    @GetMapping
        public List<ProductResponseDTO> getProduct(){
        return productService.getAll();
    }

    @GetMapping("product-size-all")
        public List<ProductSizeWithProductDTO> getAllProductSizeWithProduct(){
            return productService.getAllProductSizeWithProduct();
        }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public String createProduct(@ModelAttribute ProductRequestDTO productRequestDTO){
            productService.saveProduct(productRequestDTO);
            return "Product Added Successfully";
        }

        @PutMapping("/update-product-all")
        public ResponseEntity<?> updateMultipleProductInfo(@RequestBody List<ProductUpdateDto> product){
            if(product.isEmpty()) {
                throw new IllegalArgumentException("empty updated itmes");
            }
            try {
                productService.updateMultipleProductInfo(product);
                return new ResponseEntity<>("Updated Successfully", HttpStatus.OK);
            }catch (Exception e){
                return new ResponseEntity<>("not Updated"+e, HttpStatus.BAD_REQUEST);
            }
        }





        @DeleteMapping("{name}")
        public String deleteProduct(@PathVariable String name){
        productService.deleteProduct(name);
        return "product deleted successfully";
    }

        @DeleteMapping("/delete-image")
        public String deleteImage(
                @RequestParam Long id,
                @RequestParam String name
        ){
            productService.deleteProductImage(id,name);
            return "product deleted successfully";
        }

}
