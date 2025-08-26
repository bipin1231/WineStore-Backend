package com.winestore.winestore.service;

import com.winestore.winestore.DTO.CartItemAddRequestDTO;
import com.winestore.winestore.DTO.CartItemDTO;
import com.winestore.winestore.entity.Cart;
import com.winestore.winestore.entity.CartItem;
import com.winestore.winestore.entity.Product;
import com.winestore.winestore.entity.ProductVariant;
import com.winestore.winestore.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Repository
public class CartItemService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CartItemRepo cartItemRepo;
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductVariantRepo productVariantRepo;


//    public void addToCart(String product, int quantity) {
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////        String username = null;
////        if (authentication != null && authentication.isAuthenticated()) {
////          username= authentication.getName(); // This will return the username
////        }
////        if(userRepo.findByUsername(username).isEmpty()){
////            Cart cart=new Cart();
////            cart.setUser(userRepo.findByUsername(username).get());
//
//            cartRepo.save(cart);
//        }
//    }



public void addItemToCart(Cart cart, CartItemAddRequestDTO dto) {
    Optional<ProductVariant> sizeOpt = productVariantRepo.findById(dto.getProductVariantId());

    if (sizeOpt.isEmpty()) {
        throw new RuntimeException("Product Variant not found");
    }

    Optional<Product> productOpt = productRepo.findById(dto.getProductId());
    if (productOpt.isEmpty()) {
        throw new RuntimeException("Product not found");
    }

    Product product = productOpt.get();
    ProductVariant productVariant = sizeOpt.get();

    Optional<CartItem> cartItemOpt=cartItemRepo.findByCartAndProductAndProductVariant(cart,product, productVariant);

    if(cartItemOpt.isPresent()) {
        CartItem cartItem=cartItemOpt.get();
        cartItem.setQuantity(dto.getQuantity());
        cartItem.setTotalPrice(dto.getQuantity() * productVariant.getSellingPrice());
        cartItemRepo.save(cartItem);
    }else {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(dto.getQuantity());
        cartItem.setProductVariant(productVariant);
        cartItem.setTotalPrice(dto.getQuantity() * productVariant.getSellingPrice());

        cartItemRepo.save(cartItem);
    }



}

    public void updateCartItem(Long cartItemId, int quantity){



        CartItem existingItem=cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Product not found in cart"));
        existingItem.setQuantity(quantity);

        cartItemRepo.save(existingItem);
    }

    public List<CartItemDTO> getCartItemByUserId(Long userId) {
        // Find the cart by user ID
        Optional<Cart> cartOptional = cartRepo.findByUserId(userId);

        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();

            // Get the cart item associated with this cart
            List<CartItem> cartItem = cartItemRepo.findAllByCartId(cart.getId()); // use findFirst if multiple items

            if (!cartItem.isEmpty()) {
                return cartItem.stream().map(CartItemDTO::new).collect(Collectors.toList());
            } else {
                throw new RuntimeException("No cart item found for this cart.");
            }
        } else {
            throw new RuntimeException("No cart found for this user.");
        }
    }

    public void removeCartItem(Long cartItemId){

        cartItemRepo.deleteById(cartItemId);

    }

}