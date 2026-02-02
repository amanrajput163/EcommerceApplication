package com.example.service;

import com.example.entity.Cart;
import com.example.entity.Product;
import com.example.entity.User;
import com.example.repository.CartRepository;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
	@Autowired
    private CartRepository cartRepo;
    private  UserRepository userRepo;
    private  ProductRepository productRepo;
    public CartService(CartRepository c,UserRepository u,ProductRepository p){this.cartRepo=c;this.userRepo=u;this.productRepo=p;}

    public Cart add(String username, String productId, int quantity) {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(quantity);

        return cartRepo.save(cart);
    }

    public List<Cart> myCart(String username){
        return cartRepo.findByUser(userRepo.findByUsername(username).orElseThrow());
    }
}

