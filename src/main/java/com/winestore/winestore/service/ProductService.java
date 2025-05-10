package com.winestore.winestore.service;

import com.winestore.winestore.DTO.ProductRequestDTO;
import com.winestore.winestore.DTO.ProductResponseDTO;
import com.winestore.winestore.DTO.ProductSizeDTO;
import com.winestore.winestore.entity.Category;
import com.winestore.winestore.entity.Product;
import com.winestore.winestore.entity.ProductSize;
import com.winestore.winestore.repository.CategoryRepo;
import com.winestore.winestore.repository.ProductRepo;
import com.winestore.winestore.repository.ProductSizeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

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

    public ProductResponseDTO getProductByName(String name){
        Product p= productRepo.findByName(name).orElseThrow(()->new IllegalArgumentException("Product not found"));//stream().map(ProductResponseDTO::new).collect(Collectors.toList());
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

    public void deleteProduct(String name){
        Optional<Product> oldProduct=productRepo.findByName(name);
        oldProduct.ifPresent(product -> productRepo.delete(product));
    }




}
