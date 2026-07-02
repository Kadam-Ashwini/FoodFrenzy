package com.app.food.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.food.model.FoodItem;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

    // Admin usage (show all items)
    List<FoodItem> findByRestaurantIdOrderByIdDesc(Long restaurantId);

    // User usage (show only available items)
    List<FoodItem> findByRestaurantIdAndAvailableTrueOrderByIdDesc(Long restaurantId);

    // Search (later)
    List<FoodItem> findByNameContainingIgnoreCase(String q);
    
   

    List<FoodItem> findTop12ByCategoryIgnoreCaseAndAvailableTrueOrderByIdDesc(String category);

    List<FoodItem> findTop12ByAvailableTrueOrderByIdDesc();
    
    
    
    
    
    
    
    
    
    List<FoodItem> findTop30ByCategoryIgnoreCaseAndAvailableTrueAndRestaurantActiveTrueOrderByIdDesc(String category);

    List<FoodItem> findTop30ByAvailableTrueAndRestaurantActiveTrueOrderByIdDesc();
    
    
    
    
    
    
    List<FoodItem> findTop20ByAvailableTrueAndRestaurantActiveTrueAndNameContainingIgnoreCaseOrderByIdDesc(String q);

    List<FoodItem> findByAvailableTrueAndRestaurantActiveTrueAndCategoryIgnoreCaseOrderByIdDesc(String category);
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    List<FoodItem> findTop12ByRestaurantIdAndCategoryIgnoreCaseAndAvailableTrueOrderByIdDesc(Long restaurantId, String category);

    List<FoodItem> findTop12ByRestaurantIdAndAvailableTrueOrderByIdDesc(Long restaurantId);
    
}