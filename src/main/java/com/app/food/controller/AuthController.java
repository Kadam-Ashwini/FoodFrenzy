package com.app.food.controller;


import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.app.food.model.AppUser;
import com.app.food.model.Role;
import com.app.food.service.AuthService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam String fullName,
                             @RequestParam String email,
                             @RequestParam String phone,
                             @RequestParam String password,
                             Model model) {
        try {
            authService.registerUser(fullName, email, phone, password);
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String registered,
                            Model model) {
        if (registered != null) {
            model.addAttribute("msg", "Registration successful. Please login.");
        }
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String email,
                          @RequestParam String password,
                          Model model,
                          HttpSession session) {

        Optional<AppUser> opt = authService.authenticate(email, password);
        if (opt.isEmpty()) {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }

        AppUser u = opt.get();
        session.setAttribute("userId", u.getId());
        session.setAttribute("userName", u.getFullName());
        session.setAttribute("role", u.getRole().name());

        if (u.getRole() == Role.ADMIN) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}