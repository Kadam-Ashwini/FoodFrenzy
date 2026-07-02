
package com.app.food.repository;
import com.app.food.model.PaymentStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.food.model.FoodOrder;
import com.app.food.model.OrderStatus;

public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {

    // User
    List<FoodOrder> findByUserIdOrderByIdDesc(Long userId);
    Optional<FoodOrder> findByIdAndUserId(Long id, Long userId);

    // Admin
    List<FoodOrder> findAllByOrderByIdDesc();

    List<FoodOrder> findByStatusOrderByIdDesc(OrderStatus status);

    List<FoodOrder> findByCreatedAtBetweenOrderByIdDesc(LocalDateTime from, LocalDateTime to);

    List<FoodOrder> findByStatusAndCreatedAtBetweenOrderByIdDesc(OrderStatus status, LocalDateTime from, LocalDateTime to);
    
    
    
    
    List<FoodOrder> findByPaymentStatusAndCreatedAtBetweenOrderByIdDesc(PaymentStatus status, LocalDateTime from, LocalDateTime to);
}





 