package com.winestore.winestore.service;

import com.winestore.winestore.DTO.*;
import com.winestore.winestore.entity.Category;
import com.winestore.winestore.entity.Product;
import com.winestore.winestore.entity.ProductSize;
import com.winestore.winestore.repository.CategoryRepo;
import com.winestore.winestore.repository.ProductRepo;
import com.winestore.winestore.repository.ProductSizeRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Repository
@CrossOrigin("*")

public class ProductService {

    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductSizeRepo productSizeRepo;
    @Autowired
    private ImageService imageService;

    public ProductResponseDTO saveProduct(ProductRequestDTO dto,List<String> image){
        Category category = categoryRepo.findByName(dto.getCategory()).orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
        Product product = new Product();
        product.setName(dto.getName());
        product.setCartoonPrice(dto.getCartoonPrice());
        product.setDescription(dto.getDescription());
        product.setCategory(category);
        product.setImageUrl(image);
List<ProductSize> productSizeList= dto.getProductSize().stream().map(size-> {
                    ProductSize productSize = new ProductSize();
                    productSize.setSize(size.getSize());
                    productSize.setStock(size.getStock());
            productSize.setSellingPrice(size.getSellingPrice());
                    productSize.setCostPrice(size.getCostPrice());
                    productSize.setProduct(product);
                    return productSize;
                }
        ).toList();
        product.setProductSize(productSizeList);
         Product p=productRepo.save(product);
        return new ProductResponseDTO(p);
    }



    public List<ProductResponseDTO> getAll() {
        return productRepo.findAll()
                .stream()
                .map(ProductResponseDTO::new)
                .collect(Collectors.toList());
    }
    public List<ProductSizeWithProductDTO> getAllProductSizeWithProduct() {
        return productSizeRepo.findAll()
                .stream()
                .map(ProductSizeWithProductDTO::new)
                .collect(Collectors.toList());
    }

    public ProductResponseDTO getProductById(Long id){
        Product p= productRepo.findById(id).orElseThrow(()->new IllegalArgumentException("Product not found"));//stream().map(ProductResponseDTO::new).collect(Collectors.toList());
        return new ProductResponseDTO(p);
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
        ProductSize oldProductSize=productSizeRepo.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid product size id"));

        if(dto.getStock()!=null) oldProductSize.setStock(dto.getStock());
        if(dto.getSellingPrice()!=null) oldProductSize.setSellingPrice(dto.getSellingPrice());
        if(dto.getCostPrice()!=null) oldProductSize.setCostPrice(dto.getCostPrice());

        productSizeRepo.save(oldProductSize);

    }
@Transactional
    public void updateMultipleProductSize(List<ProductSizeDTO> dto){
        dto.forEach(size->{
            ProductSize oldProductSize=productSizeRepo.findById(size.getId()).orElseThrow(()-> new IllegalArgumentException("Invalid product size id"));
            if(size.getStock()!=null) oldProductSize.setStock(size.getStock());
            if(size.getSellingPrice()!=null) oldProductSize.setSellingPrice(size.getSellingPrice());
            if(size.getCostPrice()!=null) oldProductSize.setCostPrice(size.getCostPrice());
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




    public void deleteProduct(String name){
        Optional<Product> oldProduct=productRepo.findByName(name);
        oldProduct.ifPresent(product -> productRepo.delete(product));
    }

    public List<ProductResponseDTO> findByQuery(String query){
        return productRepo.searchByQuery(query).stream().map(ProductResponseDTO::new).collect(Collectors.toList());
    }
    public List<ProductResponseDTO> filterByQuery(String query){
        return productRepo.filterByQuery(query).stream().map(ProductResponseDTO::new).collect(Collectors.toList());
    }

    public Page<ProductSizeWithProductDTO> filterAndSort(String categoryName, Double minPrice, Double maxPrice, int page, int size, String direction) {
//        Sort sort = Sort.by("sellingPrice");
//        sort = direction.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();
        Pageable pageable = PageRequest.of(page, size);
        if(direction.equalsIgnoreCase("desc")){
            return productRepo.filterProductsDESC(categoryName,pageable).map(ProductSizeWithProductDTO::new);
        }else {
            return productRepo.filterProductsASc(categoryName,pageable).map(ProductSizeWithProductDTO::new);
        }

    }



}
