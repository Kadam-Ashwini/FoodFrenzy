package com.app.food.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.food.model.Cart;
import com.app.food.model.CartStatus;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserIdAndStatus(Long userId, CartStatus status);
}