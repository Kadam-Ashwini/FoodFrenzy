package com.app.food.tracking;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.app.food.repository.FoodOrderRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class TrackingPageController {

    private final FoodOrderRepository orderRepo;

    public TrackingPageController(FoodOrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @GetMapping("/track/{orderId}")
    public String trackPage(@PathVariable Long orderId, HttpSession session, Model model) {

        Object uid = session.getAttribute("userId");
        if (uid == null) return "redirect:/login";
        Long userId = (Long) uid;

        var order = orderRepo.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        model.addAttribute("orderId", orderId);
        model.addAttribute("orderCity", order.getCity() == null ? "" : order.getCity());
        model.addAttribute("orderStatus", order.getStatus().name());
        return "track";
    }
}