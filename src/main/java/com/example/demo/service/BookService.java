package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private static final int PAGE_SIZE = 5;

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Page<Book> getBooks(String keyword, Integer categoryId, int page, String sort) {
        Sort sortObj;
        if ("asc".equals(sort)) {
            sortObj = Sort.by("price").ascending();
        } else if ("desc".equals(sort)) {
            sortObj = Sort.by("price").descending();
        } else {
            sortObj = Sort.by("id").ascending();
        }

        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sortObj);
        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasCategory = categoryId != null;

        if (hasKeyword && hasCategory) {
            return bookRepository.findByTitleContainingIgnoreCaseAndCategory_Id(keyword.trim(), categoryId, pageable);
        } else if (hasKeyword) {
            return bookRepository.findByTitleContainingIgnoreCase(keyword.trim(), pageable);
        } else if (hasCategory) {
            return bookRepository.findByCategory_Id(categoryId, pageable);
        } else {
            return bookRepository.findAll(pageable);
        }
    }

    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
