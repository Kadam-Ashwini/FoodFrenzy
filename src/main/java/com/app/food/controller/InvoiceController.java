package com.app.food.controller;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.app.food.repository.FoodOrderRepository;
import com.app.food.repository.OrderItemRepository;
import com.app.food.repository.PaymentTransactionRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class InvoiceController {

    private final FoodOrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final PaymentTransactionRepository txRepo;

    public InvoiceController(FoodOrderRepository orderRepo,
                             OrderItemRepository orderItemRepo,
                             PaymentTransactionRepository txRepo) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.txRepo = txRepo;
    }

    private Long requireUserId(HttpSession session) {
        Object uid = session.getAttribute("userId");
        return uid == null ? null : (Long) uid;
    }

    @GetMapping("/invoice/{orderId}")
    public String invoice(@PathVariable Long orderId, HttpSession session, Model model) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        var order = orderRepo.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        model.addAttribute("order", order);
        model.addAttribute("items", orderItemRepo.findByOrderId(orderId));
        model.addAttribute("txns", txRepo.findByOrderIdOrderByIdDesc(orderId));

        return "invoice";
    }
}