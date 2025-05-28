package com.winestore.winestore.controller;

import com.winestore.winestore.DTO.ProductRequestDTO;
import com.winestore.winestore.DTO.ProductResponseDTO;
import com.winestore.winestore.DTO.ProductSizeDTO;
import com.winestore.winestore.entity.Product;
import com.winestore.winestore.repository.ProductRepo;
import com.winestore.winestore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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

@GetMapping("{id}")
    public ProductResponseDTO getProductById(@PathVariable Long id){
    return productService.getProductById(id);
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

    @DeleteMapping("{name}")
    public String deleteProduct(@PathVariable String name){
    productService.deleteProduct(name);
    return "product deleted successfully";
}

}
