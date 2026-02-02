package com.example.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter @Getter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    private int quantity;

    // getters & setters
}
