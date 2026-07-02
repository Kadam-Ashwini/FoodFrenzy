package com.app.food.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.food.model.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByActiveTrueOrderByIdDesc();

    List<Restaurant> findAllByOrderByIdDesc();

    List<Restaurant> findByNameContainingIgnoreCase(String q);
    
    
    
    
    
    
    
    
    
    List<Restaurant> findTop20ByActiveTrueAndNameContainingIgnoreCaseOrderByIdDesc(String q);
    
    
    
    
}