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

private final  String folder_path="C://Users//bipin//Downloads//winestore-images/";


@GetMapping
    public List<ProductResponseDTO> getProduct(){
    return productService.getAll();
}
    @GetMapping("product-size-all")
    public List<ProductSizeWithProductDTO> getAllProductSizeWithProduct(){
        return productService.getAllProductSizeWithProduct();
    }

@GetMapping("/search")
    public List<ProductResponseDTO> getProductByQuery(@RequestParam String query){
    return productService.findByQuery(query);
}

    @GetMapping("/filter")
    public List<ProductResponseDTO> getProductByQueryFilter(@RequestParam String query){
        return productService.filterByQuery(query);
    }

    @GetMapping("{id}")
    public ProductResponseDTO getProductById(@PathVariable Long id){
    return productService.getProductById(id);
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

@PostMapping("upload-image")
public List<String> uploadImage(@RequestParam("image") List<MultipartFile> images) throws IOException{

    List<String> imageFileName=new ArrayList<>();
    for (MultipartFile file:images){
        String fileName= UUID.randomUUID()+"_"+file.getOriginalFilename();
        Path filePath= Paths.get(folder_path+fileName);
        Files.copy(file.getInputStream(),filePath);
        imageFileName.add(fileName);

    }
  return imageFileName;

}
    @PostMapping
    public void createProduct(@RequestPart("product") ProductRequestDTO productRequestDTO,
                              @RequestPart("images") List<MultipartFile> images
                            )throws IOException {


        List<String> imageFileName=new ArrayList<>();
        for (MultipartFile file:images){
            String fileName= UUID.randomUUID()+"_"+file.getOriginalFilename();
            Path filePath= Paths.get(folder_path+fileName);
            Files.copy(file.getInputStream(),filePath);
            imageFileName.add(fileName);
        }
        productService.saveProduct(productRequestDTO,imageFileName);

    }


    @PutMapping("/update-product/{name}")
    public void updateProduct(@PathVariable String name,@RequestBody Product product){
    productService.updateProduct(name,product);
}

@PutMapping("/update-size/{id}")
public void updateProductSize(@PathVariable Long id,@RequestBody ProductSizeDTO product){
    productService.updateProductSize(id,product);
}
    @PutMapping("/update-size-all")
    public ResponseEntity<?> updateMultipleProductSize(@RequestBody List<ProductSizeDTO> product){
    if(product.isEmpty()) {
        throw new IllegalArgumentException("empty updated itmes");
    }
    try {
        productService.updateMultipleProductSize(product);
        return new ResponseEntity<>("Updated Successfully", HttpStatus.OK);
    }catch (Exception e){
        return new ResponseEntity<>("not Updated", HttpStatus.BAD_REQUEST);
    }
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


    @PutMapping(value = "/update-product-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProductImages(
            @RequestPart(value="existingImages",required = false) String existingImagesJson,
            @RequestPart(value="id") String productId,
            @RequestPart(value = "newImages", required = false) List<MultipartFile> newImages
    ) throws IOException {
        // ✅ Parse the JSON string into a list
        ObjectMapper mapper = new ObjectMapper();
        List<String> existingImages;
        try {
            existingImages = mapper.readValue(existingImagesJson, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid existingImages JSON");
        }

        // ✅ Do your update logic
        productService.updateMultipleProductImage(Long.valueOf(productId), existingImages, newImages);

        return ResponseEntity.ok("Product images updated");
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
