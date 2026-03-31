package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên sách không được để trống")
    @Column(nullable = false, length = 255)
    private String title;

    @NotBlank(message = "Tác giả không được để trống")
    @Column(nullable = false, length = 255)
    private String author;

    @NotNull(message = "Giá sách không được để trống")
    @Min(value = 1, message = "Giá sách không được nhỏ hơn 1")
    @Max(value = 9999999, message = "Giá sách không được lớn hơn 9999999")
    @Column(nullable = false)
    private Long price;

    @Length(min = 0, max = 200, message = "Tên hình ảnh không quá 200 ký tự")
    @Column(length = 200)
    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
