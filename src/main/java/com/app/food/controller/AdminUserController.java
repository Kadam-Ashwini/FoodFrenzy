package com.app.food.controller;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.app.food.repository.AppUserRepository;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final AppUserRepository userRepo;

    public AdminUserController(AppUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userRepo.findAllByOrderByIdDesc());
        return "admin-users";
    }

    @PostMapping("/{id}/toggle")
    public String toggleEnabled(@PathVariable Long id) {
        var u = userRepo.findById(id).orElseThrow();
        u.setEnabled(!u.isEnabled());
        userRepo.save(u);
        return "redirect:/admin/users";
    }
}