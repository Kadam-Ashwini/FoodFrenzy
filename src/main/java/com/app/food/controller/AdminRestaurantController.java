/*
 * package com.app.food.controller;
 * 
 * 
 * import org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.*;
 * 
 * import com.app.food.model.Restaurant; import
 * com.app.food.repository.RestaurantRepository;
 * 
 * @Controller
 * 
 * @RequestMapping("/admin/restaurants") public class AdminRestaurantController
 * {
 * 
 * private final RestaurantRepository restaurantRepo;
 * 
 * public AdminRestaurantController(RestaurantRepository restaurantRepo) {
 * this.restaurantRepo = restaurantRepo; }
 * 
 * @GetMapping public String list(Model model) {
 * model.addAttribute("restaurants", restaurantRepo.findAll()); return
 * "admin-restaurants"; }
 * 
 * @GetMapping("/new") public String newForm(Model model) {
 * model.addAttribute("restaurant", new Restaurant()); return
 * "admin-restaurant-form"; }
 * 
 * @PostMapping("/save") public String save(@ModelAttribute Restaurant
 * restaurant) { restaurantRepo.save(restaurant); return
 * "redirect:/admin/restaurants"; }
 * 
 * @GetMapping("/delete/{id}") public String delete(@PathVariable Long id) {
 * restaurantRepo.deleteById(id); return "redirect:/admin/restaurants"; } }
 */



package com.app.food.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.app.food.model.Restaurant;
import com.app.food.repository.RestaurantRepository;

@Controller
@RequestMapping("/admin/restaurants")
public class AdminRestaurantController {

    private final RestaurantRepository restaurantRepo;

    public AdminRestaurantController(RestaurantRepository restaurantRepo) {
        this.restaurantRepo = restaurantRepo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("restaurants", restaurantRepo.findAllByOrderByIdDesc());
        return "admin-restaurants";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        Restaurant r = new Restaurant();
        r.setActive(true);
        model.addAttribute("restaurant", r);
        model.addAttribute("mode", "new");
        return "admin-restaurant-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Restaurant r = restaurantRepo.findById(id).orElseThrow();
        model.addAttribute("restaurant", r);
        model.addAttribute("mode", "edit");
        return "admin-restaurant-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Restaurant restaurant) {
        // Works for both create and update (if restaurant.id exists)
        restaurantRepo.save(restaurant);
        return "redirect:/admin/restaurants";
    }

    @PostMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id) {
        Restaurant r = restaurantRepo.findById(id).orElseThrow();
        r.setActive(!r.isActive());
        restaurantRepo.save(r);
        return "redirect:/admin/restaurants";
    }
}