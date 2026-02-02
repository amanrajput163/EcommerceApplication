package com.example.service;

import com.example.entity.Cart;
import com.example.entity.Order;
import com.example.entity.User;
import com.example.repository.CartRepository;
import com.example.repository.OrderRepository;
import com.example.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final CartRepository cartRepo;
    private final UserRepository userRepo;

    public OrderService(OrderRepository o, CartRepository c, UserRepository u) {
        this.orderRepo = o;
        this.cartRepo = c;
        this.userRepo = u;
    }

    // ✅ Place order
    public Order place(String username) {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Cart> items = cartRepo.findByUser(user);

        if (items.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        double total = items.stream()
                .mapToDouble(i -> i.getProduct().getPrice() * i.getQuantity())
                .sum();

        Order order = new Order();
        order.setUser(user);
        order.setItems(items);
        order.setTotalAmount(total);
        order.setStatus("PLACED");
        order.setCreatedAt(LocalDateTime.now());

        Order saved = orderRepo.save(order);
        cartRepo.deleteAll(items);

        return saved;
    }

    // ✅ MY ORDERS WITH PAGINATION
    public Page<Order> myOrders(String username, Pageable pageable) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepo.findByUser(user, pageable);
    }

    // ✅ ADMIN – ALL ORDERS WITH PAGINATION
    public Page<Order> all(Pageable pageable) {
        return orderRepo.findAll(pageable);
    }
}
