package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_name", nullable = false, length = 100)
    private String accountName;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "total_amount")
    private Long totalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetail> details = new ArrayList<>();
}
