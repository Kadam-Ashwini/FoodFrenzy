package com.app.food.service;



import java.util.List;

import com.app.food.model.FoodItem;

public class RecommendationResult {
    private final String reason;
    private final List<FoodItem> items;

    public RecommendationResult(String reason, List<FoodItem> items) {
        this.reason = reason;
        this.items = items;
    }

    public String getReason() {
        return reason;
    }

    public List<FoodItem> getItems() {
        return items;
    }
}