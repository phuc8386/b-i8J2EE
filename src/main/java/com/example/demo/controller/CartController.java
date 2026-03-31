package com.example.demo.controller;

import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
import com.example.demo.model.Order;
import com.example.demo.model.OrderDetail;
import com.example.demo.service.BookService;
import com.example.demo.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private static final String CART_SESSION_KEY = "cart";

    @Autowired
    private BookService bookService;

    @Autowired
    private OrderService orderService;

    // Câu 6: Hiển thị trang giỏ hàng
    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        List<CartItem> cart = getCart(session);
        long total = cart.stream().mapToLong(CartItem::getSubtotal).sum();
        model.addAttribute("cartItems", cart);
        model.addAttribute("total", total);
        return "cart";
    }

    // Câu 5: Thêm sản phẩm vào giỏ hàng (lưu vào session)
    @PostMapping("/add")
    public String addToCart(@RequestParam Long bookId,
                            @RequestParam(defaultValue = "1") int quantity,
                            @RequestParam(defaultValue = "/books") String redirect,
                            HttpSession session) {
        Book book = bookService.getBookById(bookId);
        if (book == null) return "redirect:" + redirect;

        List<CartItem> cart = getCart(session);

        CartItem existing = cart.stream()
                .filter(item -> item.getBookId().equals(bookId))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
        } else {
            cart.add(new CartItem(book.getId(), book.getTitle(), book.getPrice(), book.getImage(), quantity));
        }

        session.setAttribute(CART_SESSION_KEY, cart);
        return "redirect:" + redirect;
    }

    // Cập nhật số lượng sản phẩm trong giỏ
    @PostMapping("/update")
    public String updateCart(@RequestParam Long bookId,
                             @RequestParam int quantity,
                             HttpSession session) {
        List<CartItem> cart = getCart(session);
        if (quantity <= 0) {
            cart.removeIf(item -> item.getBookId().equals(bookId));
        } else {
            cart.stream()
                    .filter(item -> item.getBookId().equals(bookId))
                    .findFirst()
                    .ifPresent(item -> item.setQuantity(quantity));
        }
        session.setAttribute(CART_SESSION_KEY, cart);
        return "redirect:/cart";
    }

    // Xóa sản phẩm khỏi giỏ
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long bookId, HttpSession session) {
        List<CartItem> cart = getCart(session);
        cart.removeIf(item -> item.getBookId().equals(bookId));
        session.setAttribute(CART_SESSION_KEY, cart);
        return "redirect:/cart";
    }

    // Câu 7: Hiển thị trang xác nhận đặt hàng
    @GetMapping("/checkout")
    public String showCheckout(HttpSession session, Model model) {
        List<CartItem> cart = getCart(session);
        if (cart.isEmpty()) return "redirect:/cart";

        long total = cart.stream().mapToLong(CartItem::getSubtotal).sum();
        model.addAttribute("cartItems", cart);
        model.addAttribute("total", total);
        return "checkout";
    }

    // Câu 7: Xử lý đặt hàng
    @PostMapping("/checkout")
    public String placeOrder(HttpSession session, Authentication authentication, Model model) {
        List<CartItem> cart = getCart(session);
        if (cart.isEmpty()) return "redirect:/cart";

        String username = authentication.getName();
        long total = cart.stream().mapToLong(CartItem::getSubtotal).sum();

        Order order = new Order();
        order.setAccountName(username);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(total);

        List<OrderDetail> details = new ArrayList<>();
        for (CartItem item : cart) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setBook(bookService.getBookById(item.getBookId()));
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getPrice());
            details.add(detail);
        }
        order.setDetails(details);

        orderService.saveOrder(order);

        session.removeAttribute(CART_SESSION_KEY);

        model.addAttribute("order", order);
        return "order-success";
    }

    @SuppressWarnings("unchecked")
    private List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }
}
