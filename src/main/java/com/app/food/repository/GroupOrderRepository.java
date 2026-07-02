package com.app.food.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.food.model.GroupOrder;

public interface GroupOrderRepository extends JpaRepository<GroupOrder, Long> {
    Optional<GroupOrder> findByCode(String code);
    boolean existsByCode(String code);
}