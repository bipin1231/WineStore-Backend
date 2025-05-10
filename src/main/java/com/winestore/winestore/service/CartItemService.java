package com.winestore.winestore.service;

import com.winestore.winestore.entity.Cart;
import com.winestore.winestore.entity.CartItem;
import com.winestore.winestore.entity.Product;
import com.winestore.winestore.repository.CartItemRepo;
import com.winestore.winestore.repository.CartRepo;
import com.winestore.winestore.repository.ProductRepo;
import com.winestore.winestore.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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


    public CartItem addItem(Cart cart,String productName,int quantity){
            //Optional<Cart> cart=cartService.getCartByUsername(username);
            Optional<Product> product=productRepo.findByName(productName);
        CartItem cartItem=new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product.get());
        cartItem.setQuantity(quantity);
        return cartItemRepo.save(cartItem);
    }
    public CartItem updateCartItem(String username, String productName,int quantity){
        Long userId=userRepo.findByUsername(username).get().getId();
        Cart cart=cartRepo.findByUserId(userId).get();

        CartItem existingItem=cart.getCartItem().stream()
                .filter(item->item.getProduct().getName().equals(productName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found in cart"));
        existingItem.setQuantity(quantity);


        cartItemRepo.save(existingItem);
        return existingItem;
    }
    public void removeCartItem(String username,String productName){
        Long userId=userRepo.findByUsername(username).get().getId();
        Cart cart=cartRepo.findByUserId(userId).get();
        CartItem existingItem=cart.getCartItem().stream()
                .filter(item->item.getProduct().getName().equals(productName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found in cart"));
        cart.getCartItem().remove(existingItem);

        cartRepo.save(cart);

    }

}