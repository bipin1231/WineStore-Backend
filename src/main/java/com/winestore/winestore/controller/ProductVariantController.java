package com.winestore.winestore.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winestore.winestore.DTO.*;
import com.winestore.winestore.entity.ProductVariant;
import com.winestore.winestore.repository.ProductRepo;
import com.winestore.winestore.service.ProductService;
import com.winestore.winestore.service.ProductVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product/variant")

public class ProductVariantController {

    @Autowired
        private ProductRepo productRepo;

    @Autowired
        private ProductService productService;

    @Autowired
    private ProductVariantService productVariantService;



@GetMapping
public List<ProductVariantResponseDto> getAllProductVariant(){
    return productVariantService.getAllProductVariant();
}


        @PostMapping(value = "/add/{productId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public String addProductVariant(@ModelAttribute ProductVariantRequestDtoWrapper dto,
                                        @PathVariable Long productId
                                            ){
            productVariantService.createProductVariant(dto.getProductVariantRequestDto(),productId);
            return "Product Added Successfully";
        }




        @PutMapping("/update")
        public ResponseEntity<?> updateProductVariant(@RequestBody List<ProductVariantUpdateDto> productVariant){
        if(productVariant.isEmpty()) {
            throw new IllegalArgumentException("empty items to updated");
        }
        try {
            productVariantService.updateMultipleProductVariant(productVariant);
            return new ResponseEntity<>("Updated Successfully", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("not Updated", HttpStatus.BAD_REQUEST);
        }
        }



        @PutMapping(value = "/update-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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


        @DeleteMapping("{id}")
        public String deleteProductVariant(@PathVariable Long id){
       productVariantService.deleteProductVariant(id);
        return "product deleted successfully";
    }
//
//        @DeleteMapping("/delete-image")
//        public String deleteImage(
//                @RequestParam Long id,
//                @RequestParam String name
//        ){
//            productService.deleteProductImage(id,name);
//            return "product deleted successfully";
//        }

}
