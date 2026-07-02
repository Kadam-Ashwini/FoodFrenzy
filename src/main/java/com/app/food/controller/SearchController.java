package com.app.food.controller;


import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.food.repository.FoodItemRepository;
import com.app.food.repository.RestaurantRepository;

@Controller
public class SearchController {

    private final RestaurantRepository restaurantRepo;
    private final FoodItemRepository foodRepo;

    public SearchController(RestaurantRepository restaurantRepo, FoodItemRepository foodRepo) {
        this.restaurantRepo = restaurantRepo;
        this.foodRepo = foodRepo;
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String q, Model model) {

        String query = (q == null) ? "" : q.trim();
        model.addAttribute("q", query);

        if (query.isEmpty()) {
            model.addAttribute("restaurants", List.of());
            model.addAttribute("foods", List.of());
            model.addAttribute("msg", "Type something to search");
            return "search-results";
        }

        model.addAttribute("restaurants",
                restaurantRepo.findTop20ByActiveTrueAndNameContainingIgnoreCaseOrderByIdDesc(query));

        model.addAttribute("foods",
                foodRepo.findTop20ByAvailableTrueAndRestaurantActiveTrueAndNameContainingIgnoreCaseOrderByIdDesc(query));

        return "search-results";
    }

    @GetMapping("/category")
    public String category(@RequestParam("name") String name, Model model) {
        String category = name == null ? "" : name.trim();
        model.addAttribute("category", category);

        if (category.isEmpty()) {
            model.addAttribute("foods", List.of());
            model.addAttribute("msg", "Category is empty");
            return "category-results";
        }

        model.addAttribute("foods",
                foodRepo.findByAvailableTrueAndRestaurantActiveTrueAndCategoryIgnoreCaseOrderByIdDesc(category));

        return "category-results";
    }
}