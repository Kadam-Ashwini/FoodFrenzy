package com.app.food.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.food.model.PaymentTransaction;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    List<PaymentTransaction> findByOrderIdOrderByIdDesc(Long orderId);
}