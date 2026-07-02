package com.app.food.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.app.food.model.DeliveryAgent;

public interface DeliveryAgentRepository extends JpaRepository<DeliveryAgent, Long> {
    List<DeliveryAgent> findByActiveTrueOrderByIdDesc();
}