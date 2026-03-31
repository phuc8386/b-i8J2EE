package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Long bookId;
    private String title;
    private Long price;
    private String image;
    private int quantity;

    public Long getSubtotal() {
        return price * quantity;
    }
}
