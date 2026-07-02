package com.app.food.config;



import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String path = request.getRequestURI(); // example: /admin/dashboard
        HttpSession session = request.getSession(false);

        // Protect admin pages
        if (path.startsWith("/admin")) {
            if (session == null || session.getAttribute("role") == null) {
                response.sendRedirect("/login");
                return false;
            }
            String role = session.getAttribute("role").toString();
            if (!"ADMIN".equals(role)) {
                response.sendRedirect("/"); // not admin -> go home
                return false;
            }
        }

        return true;
    }
}