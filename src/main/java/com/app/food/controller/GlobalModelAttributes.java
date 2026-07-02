package com.app.food.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;

@Controller
public class GlobalModelAttributes {

    @ModelAttribute("sessionUserName")
    public String sessionUserName(HttpSession session) {
        Object v = session.getAttribute("userName");
        return v == null ? null : v.toString();
    }

    @ModelAttribute("sessionRole")
    public String sessionRole(HttpSession session) {
        Object v = session.getAttribute("role");
        return v == null ? null : v.toString();
    }
}