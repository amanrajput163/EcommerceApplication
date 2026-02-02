package com.example.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Cart;
import com.example.entity.User;

public interface CartRepository extends JpaRepository<Cart,String> {
    List<Cart> findByUser(User user);}