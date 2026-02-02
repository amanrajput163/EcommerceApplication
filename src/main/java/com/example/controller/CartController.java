package com.example.controller;

import com.example.entity.Cart;
import com.example.service.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController	
@RequestMapping("/cart")
public class CartController {
@Autowired
    private  CartService service;

    
   

    // USER add product to cart
    @PostMapping("/add")
    public Cart add(@RequestParam String productId, @RequestParam int quantity, Authentication a){
        return service.add(a.getName(), productId, quantity);
    }

    // USER view own cart
    @GetMapping("/my")
    public List<Cart> my(Authentication authentication) {
        return service.myCart(authentication.getName());
    }
}
