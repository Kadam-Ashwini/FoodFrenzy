/*
 * package com.app.food.controller;
 * 
 * 
 * 
 * import org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.RequestMapping;
 * 
 * import com.app.food.repository.RestaurantFeedbackRepository;
 * 
 * @Controller
 * 
 * @RequestMapping("/admin/feedback") public class AdminFeedbackController {
 * 
 * private final RestaurantFeedbackRepository feedbackRepo;
 * 
 * public AdminFeedbackController(RestaurantFeedbackRepository feedbackRepo) {
 * this.feedbackRepo = feedbackRepo; }
 * 
 * @GetMapping public String list(Model model) { model.addAttribute("feedbacks",
 * feedbackRepo.findAllByOrderByIdDesc()); return "admin-feedback"; } }
 */


package com.app.food.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.food.repository.RestaurantFeedbackRepository;

@Controller
@RequestMapping("/admin/feedback")
public class AdminFeedbackController {

    private final RestaurantFeedbackRepository feedbackRepo;

    public AdminFeedbackController(RestaurantFeedbackRepository feedbackRepo) {
        this.feedbackRepo = feedbackRepo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("feedbacks", feedbackRepo.findAllByOrderByIdDesc());
        return "admin-feedback";
    }
}