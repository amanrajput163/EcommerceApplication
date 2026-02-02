package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    private User user;

    @OneToMany
    @JoinColumn(name = "order_id") // 🔥 important
    private List<Cart> items;

    private double totalAmount;
    private String status; // PLACED
    private LocalDateTime createdAt;
}
