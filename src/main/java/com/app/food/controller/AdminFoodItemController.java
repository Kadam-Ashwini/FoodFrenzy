/*
 * package com.app.food.controller;
 * 
 * 
 * import java.math.BigDecimal;
 * 
 * import org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.*;
 * 
 * import com.app.food.model.FoodItem; import com.app.food.model.Restaurant;
 * import com.app.food.repository.FoodItemRepository; import
 * com.app.food.repository.RestaurantRepository;
 * 
 * @Controller
 * 
 * @RequestMapping("/admin/food-items") public class AdminFoodItemController {
 * 
 * private final FoodItemRepository foodRepo; private final RestaurantRepository
 * restaurantRepo;
 * 
 * public AdminFoodItemController(FoodItemRepository foodRepo,
 * RestaurantRepository restaurantRepo) { this.foodRepo = foodRepo;
 * this.restaurantRepo = restaurantRepo; }
 * 
 * @GetMapping("/{restaurantId}") public String list(@PathVariable Long
 * restaurantId, Model model) { Restaurant r =
 * restaurantRepo.findById(restaurantId).orElseThrow();
 * model.addAttribute("restaurant", r); model.addAttribute("items",
 * foodRepo.findByRestaurantId(restaurantId)); return "admin-food-items"; }
 * 
 * @GetMapping("/{restaurantId}/new") public String newForm(@PathVariable Long
 * restaurantId, Model model) { Restaurant r =
 * restaurantRepo.findById(restaurantId).orElseThrow();
 * 
 * FoodItem item = new FoodItem(); item.setRestaurant(r);
 * item.setPrice(BigDecimal.valueOf(100)); item.setVeg(true);
 * 
 * model.addAttribute("restaurant", r); model.addAttribute("item", item); return
 * "admin-food-item-form"; }
 * 
 * @PostMapping("/{restaurantId}/save") public String save(@PathVariable Long
 * restaurantId, @ModelAttribute("item") FoodItem item) { Restaurant r =
 * restaurantRepo.findById(restaurantId).orElseThrow(); item.setRestaurant(r);
 * foodRepo.save(item); return "redirect:/admin/food-items/" + restaurantId; }
 * 
 * @GetMapping("/{restaurantId}/delete/{itemId}") public String
 * delete(@PathVariable Long restaurantId, @PathVariable Long itemId) {
 * foodRepo.deleteById(itemId); return "redirect:/admin/food-items/" +
 * restaurantId; } }
 * 
 * 
 * 
 * 
 * package com.app.food.controller;
 * 
 * import java.math.BigDecimal;
 * 
 * import org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.*;
 * 
 * import com.app.food.model.FoodItem; import com.app.food.model.Restaurant;
 * import com.app.food.repository.FoodItemRepository; import
 * com.app.food.repository.RestaurantRepository;
 * 
 * @Controller
 * 
 * @RequestMapping("/admin/food-items") public class AdminFoodItemController {
 * 
 * private final FoodItemRepository foodRepo; private final RestaurantRepository
 * restaurantRepo;
 * 
 * public AdminFoodItemController(FoodItemRepository foodRepo,
 * RestaurantRepository restaurantRepo) { this.foodRepo = foodRepo;
 * this.restaurantRepo = restaurantRepo; }
 * 
 * @GetMapping("/{restaurantId}") public String list(@PathVariable Long
 * restaurantId, Model model) { Restaurant r =
 * restaurantRepo.findById(restaurantId).orElseThrow();
 * 
 * model.addAttribute("restaurant", r); model.addAttribute("items",
 * foodRepo.findByRestaurantIdOrderByIdDesc(restaurantId)); return
 * "admin-food-items"; }
 * 
 * @GetMapping("/{restaurantId}/new") public String newForm(@PathVariable Long
 * restaurantId, Model model) { Restaurant r =
 * restaurantRepo.findById(restaurantId).orElseThrow();
 * 
 * FoodItem item = new FoodItem(); item.setRestaurant(r);
 * item.setPrice(BigDecimal.valueOf(100)); item.setVeg(true);
 * item.setAvailable(true);
 * 
 * model.addAttribute("restaurant", r); model.addAttribute("item", item);
 * model.addAttribute("mode", "new"); return "admin-food-item-form"; }
 * 
 * @GetMapping("/{restaurantId}/edit/{itemId}") public String
 * editForm(@PathVariable Long restaurantId, @PathVariable Long itemId, Model
 * model) { Restaurant r = restaurantRepo.findById(restaurantId).orElseThrow();
 * FoodItem item = foodRepo.findById(itemId).orElseThrow();
 * 
 * // safety: ensure item belongs to same restaurant if
 * (!item.getRestaurant().getId().equals(r.getId())) { throw new
 * RuntimeException("Food item does not belong to this restaurant"); }
 * 
 * model.addAttribute("restaurant", r); model.addAttribute("item", item);
 * model.addAttribute("mode", "edit"); return "admin-food-item-form"; }
 * 
 * @PostMapping("/{restaurantId}/save") public String save(@PathVariable Long
 * restaurantId, @ModelAttribute("item") FoodItem item) { Restaurant r =
 * restaurantRepo.findById(restaurantId).orElseThrow();
 * 
 * // Important: keep restaurant mapping (ignore any tampering)
 * item.setRestaurant(r);
 * 
 * foodRepo.save(item); return "redirect:/admin/food-items/" + restaurantId; }
 * 
 * @PostMapping("/{restaurantId}/toggle/{itemId}") public String
 * toggleAvailability(@PathVariable Long restaurantId, @PathVariable Long
 * itemId) { FoodItem item = foodRepo.findById(itemId).orElseThrow();
 * item.setAvailable(!item.isAvailable()); foodRepo.save(item);
 * 
 * return "redirect:/admin/food-items/" + restaurantId; } }
 */

package com.app.food.controller;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.app.food.model.FoodItem;
import com.app.food.model.Restaurant;
import com.app.food.repository.FoodItemRepository;
import com.app.food.repository.RestaurantRepository;

@Controller
@RequestMapping("/admin/food-items")
public class AdminFoodItemController {

    private final FoodItemRepository foodRepo;
    private final RestaurantRepository restaurantRepo;

    public AdminFoodItemController(FoodItemRepository foodRepo, RestaurantRepository restaurantRepo) {
        this.foodRepo = foodRepo;
        this.restaurantRepo = restaurantRepo;
    }

    @GetMapping("/{restaurantId}")
    public String list(@PathVariable Long restaurantId, Model model) {
        Restaurant r = restaurantRepo.findById(restaurantId).orElseThrow();

        model.addAttribute("restaurant", r);
        model.addAttribute("items", foodRepo.findByRestaurantIdOrderByIdDesc(restaurantId));
        return "admin-food-items";
    }

    @GetMapping("/{restaurantId}/new")
    public String newForm(@PathVariable Long restaurantId, Model model) {
        Restaurant r = restaurantRepo.findById(restaurantId).orElseThrow();

        FoodItem item = new FoodItem();
        item.setRestaurant(r);
        item.setPrice(BigDecimal.valueOf(100));
        item.setVeg(true);
        item.setAvailable(true);

        model.addAttribute("restaurant", r);
        model.addAttribute("item", item);
        model.addAttribute("mode", "new");
        return "admin-food-item-form";
    }

    @GetMapping("/{restaurantId}/edit/{itemId}")
    public String editForm(@PathVariable Long restaurantId,
                           @PathVariable Long itemId,
                           Model model) {
        Restaurant r = restaurantRepo.findById(restaurantId).orElseThrow();
        FoodItem item = foodRepo.findById(itemId).orElseThrow();

        if (!item.getRestaurant().getId().equals(r.getId())) {
            throw new RuntimeException("Food item does not belong to this restaurant");
        }

        model.addAttribute("restaurant", r);
        model.addAttribute("item", item);
        model.addAttribute("mode", "edit");
        return "admin-food-item-form";
    }

    @PostMapping("/{restaurantId}/save")
    public String save(@PathVariable Long restaurantId, @ModelAttribute("item") FoodItem item) {
        Restaurant r = restaurantRepo.findById(restaurantId).orElseThrow();

        // Always set restaurant server-side
        item.setRestaurant(r);

        foodRepo.save(item);
        return "redirect:/admin/food-items/" + restaurantId;
    }

    @PostMapping("/{restaurantId}/toggle/{itemId}")
    public String toggleAvailability(@PathVariable Long restaurantId, @PathVariable Long itemId) {
        FoodItem item = foodRepo.findById(itemId).orElseThrow();
        item.setAvailable(!item.isAvailable());
        foodRepo.save(item);

        return "redirect:/admin/food-items/" + restaurantId;
    }
}