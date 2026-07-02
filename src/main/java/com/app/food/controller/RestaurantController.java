/*
 * package com.app.food.controller;
 * 
 * import org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.*;
 * 
 * import com.app.food.repository.FoodItemRepository; import
 * com.app.food.repository.RestaurantRepository;
 * 
 * @Controller public class RestaurantController {
 * 
 * private final RestaurantRepository restaurantRepo; private final
 * FoodItemRepository foodRepo;
 * 
 * public RestaurantController(RestaurantRepository restaurantRepo,
 * FoodItemRepository foodRepo) { this.restaurantRepo = restaurantRepo;
 * this.foodRepo = foodRepo; }
 * 
 * @GetMapping("/restaurants/{id}") public String restaurantMenu(@PathVariable
 * Long id, Model model) { var restaurant =
 * restaurantRepo.findById(id).orElseThrow();
 * 
 * // Only available food items for users: var items =
 * foodRepo.findByRestaurantIdAndAvailableTrueOrderByIdDesc(id);
 * 
 * model.addAttribute("restaurant", restaurant); model.addAttribute("items",
 * items); return "restaurant-menu"; } }
 */

package com.app.food.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.app.food.repository.FoodItemRepository;
import com.app.food.repository.RestaurantRepository;
import com.app.food.service.RecommendationService;

import jakarta.servlet.http.HttpSession;

@Controller
public class RestaurantController {

    private final RestaurantRepository restaurantRepo;
    private final FoodItemRepository foodRepo;
    private final RecommendationService recommendationService;

    public RestaurantController(RestaurantRepository restaurantRepo,
                                FoodItemRepository foodRepo,
                                RecommendationService recommendationService) {
        this.restaurantRepo = restaurantRepo;
        this.foodRepo = foodRepo;
        this.recommendationService = recommendationService;
    }

    @GetMapping("/restaurants/{id}")
    public String restaurantMenu(@PathVariable Long id, Model model, HttpSession session) {
        var restaurant = restaurantRepo.findById(id).orElseThrow();

        // User menu items (only available):
        var items = foodRepo.findByRestaurantIdAndAvailableTrueOrderByIdDesc(id);

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("items", items);

        Object uid = session.getAttribute("userId");
        if (uid != null) {
            Long userId = (Long) uid;
            var rec = recommendationService.recommendForUserInRestaurant(userId, id);
            model.addAttribute("menuRecommendedItems", rec.getItems());
            model.addAttribute("menuRecommendReason", rec.getReason());
        }

        return "restaurant-menu";
    }
}