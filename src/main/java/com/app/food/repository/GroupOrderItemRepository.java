package com.app.food.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.food.model.GroupOrderItem;

public interface GroupOrderItemRepository extends JpaRepository<GroupOrderItem, Long> {
    List<GroupOrderItem> findByGroupOrderId(Long groupOrderId);
    Optional<GroupOrderItem> findByGroupOrderIdAndFoodItemIdAndAddedById(Long groupOrderId, Long foodItemId, Long addedById);
    void deleteByGroupOrderId(Long groupOrderId);
}