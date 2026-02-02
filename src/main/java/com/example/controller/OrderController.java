package com.example.controller;

import com.example.entity.Order;
import com.example.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService s) {
        this.service = s;
    }

    // ✅ PLACE ORDER
    @PostMapping("/place")
    public Order place(Authentication auth) {
        return service.place(auth.getName());
    }

    // ✅ USER – MY ORDERS (PAGINATION)
    @GetMapping("/my")
    public Page<Order> myOrders(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return service.myOrders(auth.getName(), pageable);
    }

    // ✅ ADMIN – ALL ORDERS (PAGINATION)
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<Order> all(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return service.all(pageable);
    }
}
