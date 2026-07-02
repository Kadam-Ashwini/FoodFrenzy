/*
 * package com.app.food.controller;
 * 
 * 
 * 
 * import java.util.List;
 * 
 * import org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.GetMapping;
 * 
 * @Controller public class HomeController {
 * 
 * @GetMapping("/") public String home(Model model) {
 * model.addAttribute("cities", List.of("Hyderabad", "Bangalore", "Chennai",
 * "Mumbai", "Delhi")); return "home"; } }
 * 
 * 
 * 
 * package com.app.food.controller;
 * 
 * import java.util.List;
 * 
 * import org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.GetMapping;
 * 
 * import com.app.food.repository.RestaurantRepository;
 * 
 * @Controller public class HomeController {
 * 
 * private final RestaurantRepository restaurantRepo;
 * 
 * public HomeController(RestaurantRepository restaurantRepo) {
 * this.restaurantRepo = restaurantRepo; }
 * 
 * @GetMapping("/") public String home(Model model) {
 * model.addAttribute("cities", List.of("Hyderabad", "Bangalore", "Chennai",
 * "Mumbai", "Delhi")); model.addAttribute("restaurants",
 * restaurantRepo.findByActiveTrueOrderByIdDesc()); return "home"; } }
 */

/*
 * package com.app.food.controller;
 * 
 * import java.util.List;
 * 
 * import org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.GetMapping;
 * 
 * import com.app.food.repository.RestaurantRepository; import
 * com.app.food.service.RecommendationService;
 * 
 * import jakarta.servlet.http.HttpSession;
 * 
 * @Controller public class HomeController {
 * 
 * private final RestaurantRepository restaurantRepo; private final
 * RecommendationService recommendationService;
 * 
 * public HomeController(RestaurantRepository restaurantRepo,
 * RecommendationService recommendationService) { this.restaurantRepo =
 * restaurantRepo; this.recommendationService = recommendationService; }
 * 
 * @GetMapping("/") public String home(Model model, HttpSession session) {
 * model.addAttribute("cities", List.of("Hyderabad", "Bangalore", "Chennai",
 * "Mumbai", "Delhi")); model.addAttribute("restaurants",
 * restaurantRepo.findByActiveTrueOrderByIdDesc());
 * 
 * 
 * Object uid = session.getAttribute("userId"); if (uid != null) { Long userId =
 * (Long) uid; model.addAttribute("recommendedItems",
 * recommendationService.recommendForUser(userId)); }
 * 
 * 
 * 
 * Object uid = session.getAttribute("userId"); if (uid != null) { Long userId =
 * (Long) uid; var rec = recommendationService.recommendSectionForUser(userId);
 * model.addAttribute("recommendedItems", rec.getItems());
 * model.addAttribute("recommendReason", rec.getReason()); }
 * 
 * 
 * return "home"; } }
 */



package com.app.food.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.app.food.repository.RestaurantRepository;
import com.app.food.service.RecommendationService;


import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    private final RestaurantRepository restaurantRepo;
    private final RecommendationService recommendationService;

    public HomeController(RestaurantRepository restaurantRepo, RecommendationService recommendationService) {
        this.restaurantRepo = restaurantRepo;
        this.recommendationService = recommendationService;
    }

    @GetMapping("/")
    public String home(Model model, HttpSession session) {

        // show location ONLY on home page
        model.addAttribute("showLocation", true);

        // dropdown cities data
//        model.addAttribute("cities", List.of("Hyderabad", "Bangalore", "Chennai", "Mumbai", "Delhi", "Nashik", "Pune", "Nagpur"));
        model.addAttribute("cities", List.of("Nashik", "Pune", "Nagpur", "Mumbai", "Delhi","Hyderabad", "Bangalore", "Chennai"));
        // home restaurants
        model.addAttribute("restaurants", restaurantRepo.findByActiveTrueOrderByIdDesc());

        // recommendations
        Object uid = session.getAttribute("userId");
        if (uid != null) {
            Long userId = (Long) uid;
            var rec = recommendationService.recommendSectionForUser(userId);
            model.addAttribute("recommendedItems", rec.getItems());
            model.addAttribute("recommendReason", rec.getReason());
        }

        return "home";
    }
}