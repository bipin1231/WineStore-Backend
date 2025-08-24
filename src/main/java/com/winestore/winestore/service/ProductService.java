package com.winestore.winestore.service;

import com.winestore.winestore.DTO.*;
import com.winestore.winestore.entity.Category;
import com.winestore.winestore.entity.Product;
import com.winestore.winestore.entity.ProductVariant;
import com.winestore.winestore.repository.CategoryRepo;
import com.winestore.winestore.repository.ProductRepo;
import com.winestore.winestore.repository.ProductVariantRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@CrossOrigin("*")

public class ProductService {

    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductVariantRepo productVariantRepo;

    @Autowired
    private ImageService imageService;
    @Autowired
    private ProductVariantService productVariantService;

    public void saveProduct(ProductRequestDTO dto){
        Category category = categoryRepo.findByName(dto.getCategory()).orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setCategory(category);
        Product savedProduct = productRepo.save(product);
        if(!dto.getProductVariantRequestDto().isEmpty()) {
           productVariantService.createProductVariant(dto.getProductVariantRequestDto(), savedProduct.getId());

        }


    }



    public List<ProductResponseDTO> getAll() {
        return productRepo.findAll()
                .stream()
                .map(ProductResponseDTO::new)
                .collect(Collectors.toList());
    }
    public ProductResponseDTO getAllProductById(Long id) {

            Product product= productRepo.findById(id)
                  .orElseThrow(() -> new IllegalArgumentException("Invalid Product ID"));
            return new ProductResponseDTO(product);

    }
    public List<ProductSizeWithProductDTO> getAllProductSizeWithProduct() {
        return productVariantRepo.findAll()
                .stream()
                .map(ProductSizeWithProductDTO::new)
                .collect(Collectors.toList());
    }


    public void updateProduct(String name, Product newProduct){
        Optional<Product> oldProduct=productRepo.findByName(name);

        if(oldProduct.isPresent()){
            Product product=oldProduct.get();
            String nam=!(newProduct.getName().isEmpty())?newProduct.getName():oldProduct.get().getName();
            product.setName(nam);
        //    product.setPrice(newProduct.getPrice());
            product.setDescription(newProduct.getDescription());
            productRepo.save(product);
        }

    }

    public void updateProductSize(Long id, ProductSizeDTO dto){
        ProductVariant oldProductVariant = productVariantRepo.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid product size id"));

        if(dto.getStock()!=null) oldProductVariant.setStock(dto.getStock());
        if(dto.getSellingPrice()!=null) oldProductVariant.setSellingPrice(dto.getSellingPrice());
        if(dto.getCostPrice()!=null) oldProductVariant.setCostPrice(dto.getCostPrice());

        productVariantRepo.save(oldProductVariant);

    }
@Transactional
    public void updateMultipleProductSize(List<ProductSizeDTO> dto){
        dto.forEach(size->{
            ProductVariant oldProductVariant = productVariantRepo.findById(size.getId()).orElseThrow(()-> new IllegalArgumentException("Invalid product size id"));
            if(size.getStock()!=null) oldProductVariant.setStock(size.getStock());
            if(size.getSellingPrice()!=null) oldProductVariant.setSellingPrice(size.getSellingPrice());
            if(size.getCostPrice()!=null) oldProductVariant.setCostPrice(size.getCostPrice());
        });

    }

    @Transactional
    public void updateMultipleProductInfo(List<ProductUpdateDto> dto){
        dto.forEach(p->{
            Product oldProduct=productRepo.findById(p.getId()).orElseThrow(()-> new IllegalArgumentException("Invalid product id"));
            if(p.getCategoryName()!=null) {
                Category category = categoryRepo.findByName(p.getCategoryName()).orElseThrow(() -> new IllegalArgumentException("Invalid category"));
                oldProduct.setCategory(category);
            }
            if(p.getName()!=null) oldProduct.setName(p.getName());
            if(p.getDescription()!=null) oldProduct.setDescription(p.getDescription());


        });

    }


    @Transactional
    public void updateMultipleProductImage(Long id,List<String> existingImage,List<MultipartFile> newImages) throws IOException {

            Product oldProduct=productRepo.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid product id"));
            List<String> oldImage=oldProduct.getImageUrl();

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

    oldProduct.setImageUrl(commonImages);

    }


    public void deleteProductImage(Long id, String name) {
        Product oldProduct = productRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product id"));

        List<String> images = oldProduct.getImageUrl();
        boolean imageDeleted = imageService.deleteImage(name);
        boolean removed = images.removeIf(imageUrl -> imageUrl.equals(name));

        if (!removed) {
            throw new IllegalArgumentException("Image name not found: " + name);
        }
        if (imageDeleted) {
            oldProduct.setImageUrl(images);
            productRepo.save(oldProduct);
        }
    }


    public void deleteProduct(Long id){
        Optional<Product> oldProduct=productRepo.findById(id);
        oldProduct.ifPresent(product -> productRepo.delete(product));
    }




    public List<String> getMatchingNamesBySearchQuery(String query){
        return productRepo.getMatchingNamesBySearchQuery(query);
    }
    public List<ProductSizeWithProductDTO> findByQuery(String query){
        return productRepo.searchByQuery(query).stream().map(ProductSizeWithProductDTO::new).toList();
    }
    public List<ProductResponseDTO> filterByQuery(String query){
        return productRepo.filterByQuery(query).stream().map(ProductResponseDTO::new).collect(Collectors.toList());
    }


    public Page<ProductSizeWithProductDTO> filterAndSort(String categoryName, Double minPrice, Double maxPrice, int page, int size, String direction) {

        if(categoryName.equals("undefined") || categoryName.equals("null")){
            categoryName=null;
        }
        Pageable pageable = PageRequest.of(page, size);
        if(direction.equalsIgnoreCase("desc")){
            return productRepo.filterProductsDESC(categoryName,pageable).map(ProductSizeWithProductDTO::new);
        }else {
            return productRepo.filterProductsASc(categoryName,pageable).map(ProductSizeWithProductDTO::new);
        }

    }



}
