/*
  package com.app.food.controller;
  
  
  
  import org.springframework.stereotype.Controller; import
  org.springframework.web.bind.annotation.GetMapping; import
  org.springframework.web.bind.annotation.RequestMapping;
  
  @Controller
  
  @RequestMapping("/admin") public class AdminController {
  
  @GetMapping("/dashboard") public String dashboard() { return
  "admin-dashboard"; } }
  */
  
  // FILE: src/main/java/com/app/food/controller/AdminController.java package
  
   package com.app.food.controller;
  
  import org.springframework.stereotype.Controller; import
  org.springframework.ui.Model; import
  org.springframework.web.bind.annotation.GetMapping; import
  org.springframework.web.bind.annotation.RequestMapping;
  
  import com.app.food.repository.AppUserRepository; import
  com.app.food.repository.FoodOrderRepository; import
  com.app.food.repository.RestaurantFeedbackRepository; import
  com.app.food.repository.RestaurantRepository;
  
  @Controller
  
  @RequestMapping("/admin") public class AdminController {
  
  private final RestaurantRepository restaurantRepo; private final
  FoodOrderRepository orderRepo; private final AppUserRepository userRepo;
  private final RestaurantFeedbackRepository feedbackRepo;
  
  public AdminController(RestaurantRepository restaurantRepo,
  FoodOrderRepository orderRepo, AppUserRepository userRepo,
  RestaurantFeedbackRepository feedbackRepo) { this.restaurantRepo =
  restaurantRepo; this.orderRepo = orderRepo; this.userRepo = userRepo;
  this.feedbackRepo = feedbackRepo; }
  
  @GetMapping("/dashboard") public String dashboard(Model model) {
  model.addAttribute("restaurantsCount", restaurantRepo.count());
  model.addAttribute("ordersCount", orderRepo.count());
  model.addAttribute("usersCount", userRepo.count());
  model.addAttribute("feedbackCount", feedbackRepo.count()); return
  "admin-dashboard"; } }
 


