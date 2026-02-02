package com.example.service;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.entity.Product;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.repository.ProductRepository;
import com.example.repository.UserRepository;

@Service
public class ProductService {

    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    public ProductService(ProductRepository productRepo,
                          UserRepository userRepo) {
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

    // without pagination
    public List<Product> all() {
        return productRepo.findAll();
    }

    // ✅ WITH pagination
    public Page<Product> all(Pageable pageable) {
        return productRepo.findAll(pageable);
    }

    public Product byId(String id) {
        return productRepo.findById(id).orElse(null);
    }

    public Product add(Product p, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only ADMIN can add products");
        }

        p.setAddedBy(user);
        p.setCreatedAt(LocalDateTime.now());
        return productRepo.save(p);
    }
}
