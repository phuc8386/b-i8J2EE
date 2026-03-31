package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryService categoryService;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private String saveImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String ext = "";
        String original = file.getOriginalFilename();
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID() + ext;
        Files.copy(file.getInputStream(), uploadPath.resolve(fileName));
        return "/" + uploadDir + "/" + fileName;
    }

    // Câu 1, 2, 3, 4: Tìm kiếm + phân trang + sắp xếp + lọc category
    @GetMapping
    public String listBooks(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String sort,
            Model model) {

        Page<Book> bookPage = bookService.getBooks(keyword, categoryId, page, sort);

        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("bookPage", bookPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("sort", sort);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "book/list";
    }

    // Hien thi form them sach moi
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "book/add";
    }

    // Luu sach moi
    @PostMapping("/save")
    public String saveBook(@ModelAttribute("book") Book book,
                           @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            book.setImage(saveImage(imageFile));
        }
        bookService.saveBook(book);
        return "redirect:/books";
    }

    // Hien thi form chinh sua sach
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "book/edit";
    }

    // Cap nhat sach
    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable("id") Long id,
                             @ModelAttribute("book") Book book,
                             @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        book.setId(id);
        if (!imageFile.isEmpty()) {
            book.setImage(saveImage(imageFile));
        } else {
            // Giu lai anh cu neu khong chon anh moi
            Book existing = bookService.getBookById(id);
            if (existing != null) {
                book.setImage(existing.getImage());
            }
        }
        bookService.saveBook(book);
        return "redirect:/books";
    }

    // Xoa sach
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}

