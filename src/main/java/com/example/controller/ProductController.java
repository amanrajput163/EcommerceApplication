package com.example.controller;

import com.example.entity.Product;
import com.example.service.ProductService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    // ✅ Get all products (without pagination)
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.all();
    }

    // ✅ Get products WITH pagination
    @GetMapping("/page")
    public Page<Product> getProductsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
	    ) {
	        Pageable pageable = PageRequest.of(page, size);
	        return productService.all(pageable);
	    }

    // ✅ Get product by id
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable String id) {
        return productService.byId(id);
    }

    // ✅ ADMIN add product
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public Product add(@RequestBody Product p, Authentication a){
        return productService.add(p, a.getName());
    }
}
