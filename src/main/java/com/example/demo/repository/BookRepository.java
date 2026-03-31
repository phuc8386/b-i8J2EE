package com.example.demo.repository;

import com.example.demo.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Book> findByCategory_Id(Integer categoryId, Pageable pageable);

    Page<Book> findByTitleContainingIgnoreCaseAndCategory_Id(String title, Integer categoryId, Pageable pageable);
}
