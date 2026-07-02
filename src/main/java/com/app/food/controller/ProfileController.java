package com.app.food.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.app.food.model.UserAddress;
import com.app.food.service.ProfileService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    private Long requireUserId(HttpSession session) {
        Object uid = session.getAttribute("userId");
        return uid == null ? null : (Long) uid;
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        model.addAttribute("user", profileService.getUser(userId));
        model.addAttribute("addresses", profileService.addresses(userId));
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String fullName,
                                @RequestParam String phone,
                                HttpSession session) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        profileService.updateProfile(userId, fullName, phone);

        // update navbar hello name immediately
        session.setAttribute("userName", fullName);

        return "redirect:/profile?ok=Profile%20updated";
    }

    // ---- Address ----
    @GetMapping("/profile/address/new")
    public String newAddress(HttpSession session, Model model) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        UserAddress addr = new UserAddress();
        addr.setLabel("Home");
        model.addAttribute("address", addr);
        model.addAttribute("mode", "new");
        return "address-form";
    }

    @GetMapping("/profile/address/edit/{id}")
    public String editAddress(@PathVariable Long id, HttpSession session, Model model) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        model.addAttribute("address", profileService.addressForEdit(userId, id));
        model.addAttribute("mode", "edit");
        return "address-form";
    }

    @PostMapping("/profile/address/save")
    public String saveAddress(@ModelAttribute("address") UserAddress address, HttpSession session) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        profileService.saveAddress(userId, address);
        return "redirect:/profile?ok=Address%20saved";
    }

    @PostMapping("/profile/address/delete/{id}")
    public String deleteAddress(@PathVariable Long id, HttpSession session) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        profileService.deleteAddress(userId, id);
        return "redirect:/profile?ok=Address%20deleted";
    }

    @PostMapping("/profile/address/default/{id}")
    public String setDefault(@PathVariable Long id, HttpSession session) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        profileService.setDefault(userId, id);
        return "redirect:/profile?ok=Default%20address%20updated";
    }
}