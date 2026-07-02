package com.app.food.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.food.model.FoodOrder;
import com.app.food.model.OrderStatus;
import com.app.food.model.RestaurantFeedback;
import com.app.food.repository.AppUserRepository;
import com.app.food.repository.FoodOrderRepository;
import com.app.food.repository.RestaurantFeedbackRepository;

@Service
public class FeedbackService {

    private final FoodOrderRepository orderRepo;
    private final RestaurantFeedbackRepository feedbackRepo;
    private final AppUserRepository userRepo;

    public FeedbackService(FoodOrderRepository orderRepo,
                           RestaurantFeedbackRepository feedbackRepo,
                           AppUserRepository userRepo) {
        this.orderRepo = orderRepo;
        this.feedbackRepo = feedbackRepo;
        this.userRepo = userRepo;
    }

    public boolean alreadySubmitted(Long orderId) {
        return feedbackRepo.existsByOrderId(orderId);
    }

    public RestaurantFeedback getByOrderId(Long orderId) {
        return feedbackRepo.findByOrderId(orderId).orElse(null);
    }

    public FoodOrder getOrderForUser(Long orderId, Long userId) {
        return orderRepo.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    public void submitFeedback(Long orderId, Long userId, int rating, String comment) {

        if (feedbackRepo.existsByOrderId(orderId)) {
            throw new RuntimeException("Feedback already submitted for this order.");
        }

        FoodOrder order = getOrderForUser(orderId, userId);

        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new RuntimeException("Feedback allowed only after delivery.");
        }

        if (rating < 1 || rating > 5) {
            throw new RuntimeException("Rating must be between 1 and 5.");
        }

        RestaurantFeedback fb = new RestaurantFeedback();
        fb.setOrder(order);
        fb.setUser(userRepo.findById(userId).orElseThrow());
        fb.setRestaurant(order.getRestaurant());
        fb.setRating(rating);
        fb.setComment(comment == null ? null : comment.trim());

        feedbackRepo.save(fb);
    }
}