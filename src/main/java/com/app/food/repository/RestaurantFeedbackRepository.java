package com.app.food.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.food.model.RestaurantFeedback;

public interface RestaurantFeedbackRepository extends JpaRepository<RestaurantFeedback, Long> {

    boolean existsByOrderId(Long orderId);

    Optional<RestaurantFeedback> findByOrderId(Long orderId);

    List<RestaurantFeedback> findByRestaurantIdOrderByIdDesc(Long restaurantId);

    List<RestaurantFeedback> findAllByOrderByIdDesc();
}