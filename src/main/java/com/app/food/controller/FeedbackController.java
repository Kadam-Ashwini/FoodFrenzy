package com.app.food.controller;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.app.food.service.FeedbackService;

import jakarta.servlet.http.HttpSession;

@Controller
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    private Long requireUserId(HttpSession session) {
        Object uid = session.getAttribute("userId");
        return uid == null ? null : (Long) uid;
    }

    @GetMapping("/feedback/order/{orderId}")
    public String feedbackForm(@PathVariable Long orderId, HttpSession session, Model model) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        var order = feedbackService.getOrderForUser(orderId, userId);
        var existing = feedbackService.getByOrderId(orderId);

        model.addAttribute("order", order);
        model.addAttribute("existing", existing); // if already submitted
        return "feedback-form";
    }

    @PostMapping("/feedback/order/{orderId}")
    public String submit(@PathVariable Long orderId,
                         @RequestParam int rating,
                         @RequestParam(required = false) String comment,
                         HttpSession session) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        try {
            feedbackService.submitFeedback(orderId, userId, rating, comment);
            return "redirect:/orders/" + orderId + "?ok=Feedback%20submitted";
        } catch (Exception e) {
            String msg = (e.getMessage() == null ? "Error" : e.getMessage()).replace(" ", "%20");
            return "redirect:/feedback/order/" + orderId + "?msg=" + msg;
        }
    }
}