package com.app.food.controller;


import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.app.food.model.Cart;
import com.app.food.model.CartItem;
import com.app.food.service.CartService;
import com.app.food.service.OrderService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;

    public CheckoutController(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    private Long requireUserId(HttpSession session) {
        Object uid = session.getAttribute("userId");
        return uid == null ? null : (Long) uid;
    }

    @GetMapping("/checkout")
    public String checkoutPage(HttpSession session, Model model) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        Cart cart = cartService.getOpenCartOrNull(userId);
        if (cart == null) return "redirect:/cart?msg=Your%20cart%20is%20empty";

        List<CartItem> items = cartService.getItems(cart.getId());
        if (items.isEmpty()) return "redirect:/cart?msg=Your%20cart%20is%20empty";

        model.addAttribute("cart", cart);
        model.addAttribute("items", items);
        model.addAttribute("total", cartService.calculateTotal(items));
        model.addAttribute("cities", List.of("Nashik", "Pune", "Nagpur", "Mumbai", "Delhi","Hyderabad", "Bangalore", "Chennai"));
        return "checkout";
    }

    @PostMapping("/checkout/place")
    public String placeOrder(@RequestParam String deliveryAddress,
                             @RequestParam String city,
                             HttpSession session,
                             Model model) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        if (deliveryAddress == null || deliveryAddress.trim().length() < 5) {
            return "redirect:/checkout?msg=Please%20enter%20delivery%20address";
        }

        Long orderId = orderService.placeOrder(userId, deliveryAddress.trim(), city);
//        return "redirect:/order-placed/" + orderId;
        return "redirect:/payment/" + orderId;
    }

    @GetMapping("/order-placed/{orderId}")
    public String orderPlaced(@PathVariable Long orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "order-placed";
    }
}