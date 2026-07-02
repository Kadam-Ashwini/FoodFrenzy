package com.app.food.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.app.food.service.OrderTrackingService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DeliveryConfirmController {

    private final OrderTrackingService orderService;

    public DeliveryConfirmController(OrderTrackingService orderService) {
        this.orderService = orderService;
    }

    private Long requireUserId(HttpSession session) {
        Object uid = session.getAttribute("userId");
        return uid == null ? null : (Long) uid;
    }

    @PostMapping("/orders/{id}/confirm-delivered")
    public String confirmDelivered(@PathVariable Long id, HttpSession session) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        try {
            orderService.userConfirmDelivered(id, userId);
            return "redirect:/orders/" + id + "?ok=Delivery%20confirmed";
        } catch (Exception e) {
            String msg = (e.getMessage() == null ? "Error" : e.getMessage()).replace(" ", "%20");
            return "redirect:/orders/" + id + "?msg=" + msg;
        }
    }
}