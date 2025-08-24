package com.winestore.winestore.service;

import com.winestore.winestore.DTO.ProductSizeDTO;
import com.winestore.winestore.DTO.ProductVariantRequestDto;
import com.winestore.winestore.DTO.ProductVariantResponseDto;
import com.winestore.winestore.DTO.ProductVariantUpdateDto;
import com.winestore.winestore.entity.Product;
import com.winestore.winestore.entity.ProductVariant;
import com.winestore.winestore.entity.Size;
import com.winestore.winestore.repository.ProductRepo;
import com.winestore.winestore.repository.SizeRepo;
import com.winestore.winestore.repository.ProductVariantRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductVariantService {
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductVariantRepo productVariantRepo;

    @Autowired
    private SizeRepo sizeRepo;

    @Autowired
    private ImageService imageService;

    public List<ProductVariantResponseDto> getAllProductVariant(){
        return productVariantRepo.findAll().stream().map(ProductVariantResponseDto::new).toList();
    }

    public void createProductVariant(List<ProductVariantRequestDto> productVariant, Long id){
        Product product=productRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Product ID"));

       List<ProductVariant> oldProductVariant=product.getProductVariant();
       if(oldProductVariant==null){
           oldProductVariant=new ArrayList<>();
       }
        List<ProductVariant> newProductVariant= productVariant.stream().map(variant->{
            try {
                ProductVariant newVariant = new ProductVariant();
                Size size = sizeRepo.findById(variant.getSizeId()).orElseThrow(() -> new IllegalArgumentException("Invalid size ID"));
                newVariant.setProduct(product);
                newVariant.setSize(size);
                newVariant.setCostPrice(variant.getCostPrice());
                newVariant.setSellingPrice(variant.getSellingPrice());
                newVariant.setCartoonCostPrice(variant.getCartoonCostPrice());
                newVariant.setCartoonSellingPrice(variant.getCartoonSellingPrice());
                newVariant.setStock(variant.getStock());
                List<String> imageUrls = imageService.addMultipleImage(variant.getImageUrl());

                newVariant.setImageUrl(imageUrls);

                return newVariant;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toCollection(ArrayList::new));

        oldProductVariant.addAll(newProductVariant);
        product.setProductVariant(oldProductVariant);
        productRepo.save(product);

    }

    @Transactional
    public void updateMultipleProductVariant(List<ProductVariantUpdateDto> dto){
        dto.forEach(variant->{


            ProductVariant oldProductVariant = productVariantRepo.findById(variant.getId()).orElseThrow(()-> new IllegalArgumentException("Invalid product size id"));
//            if(variant.getSizeId()!=null){
//                Size size=sizeRepo.findById(variant.getId()).orElseThrow(()->new IllegalArgumentException("invalid size id"));
//                oldProductVariant.setSize(size);
//            }

            if(variant.getStock()!=null) oldProductVariant.setStock(variant.getStock());
            if(variant.getSellingPrice()!=null) oldProductVariant.setSellingPrice(variant.getSellingPrice());
            if(variant.getCostPrice()!=null) oldProductVariant.setCostPrice(variant.getCostPrice());
            if(variant.getCartoonCostPrice()!=null) oldProductVariant.setCartoonCostPrice(variant.getCartoonCostPrice());
            if(variant.getCartoonSellingPrice()!=null) oldProductVariant.setCartoonSellingPrice(variant.getCartoonSellingPrice());




        });

    }

    @Transactional
    public void updateMultipleProductVariantImage(Long id,List<String> existingImage,List<MultipartFile> newImages) throws IOException {

        ProductVariant oldProductVariant=productVariantRepo.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid product id"));

        List<String> oldImage=oldProductVariant.getImageUrl();

        oldImage.forEach(url->{
            if(!existingImage.contains(url)){
                imageService.deleteImage(url);
            }

        });
        List<String> commonImages = oldImage.stream()
                .filter(existingImage::contains)
                .collect(Collectors.toCollection(ArrayList::new));

        if (newImages != null && !newImages.isEmpty()) {
            List<String> newImageFileName=imageService.addMultipleImage(newImages);
            commonImages.addAll(newImageFileName);
        }

        oldProductVariant.setImageUrl(commonImages);

    }
    public void deleteProductVariant(Long id){
        productVariantRepo.deleteById(id);
    }


}
