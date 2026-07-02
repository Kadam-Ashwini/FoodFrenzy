package com.app.food.controller;


import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.app.food.model.Cart;
import com.app.food.model.CartItem;
import com.app.food.service.CartService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    private Long requireUserId(HttpSession session) {
        Object uid = session.getAttribute("userId");
        return uid == null ? null : (Long) uid;
    }

    @PostMapping("/cart/add")
    public String add(@RequestParam Long foodItemId,
                      @RequestParam(required = false) Long restaurantId,
                      HttpSession session,
                      Model model) {

        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        String err = cartService.addToCart(userId, foodItemId);
        if (err != null) {
            // send user back to restaurant page with message
            return "redirect:/restaurants/" + restaurantId + "?msg=" + err.replace(" ", "%20");
        }

        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String viewCart(@RequestParam(required = false) String msg,
                           HttpSession session,
                           Model model) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        Cart cart = cartService.getOpenCartOrNull(userId);
        if (cart == null) {
            model.addAttribute("items", List.of());
            model.addAttribute("total", 0);
            model.addAttribute("msg", msg);
            return "cart";
        }

        List<CartItem> items = cartService.getItems(cart.getId());
        model.addAttribute("cart", cart);
        model.addAttribute("items", items);
        model.addAttribute("total", cartService.calculateTotal(items));
        model.addAttribute("msg", msg);
        return "cart";
    }

    @PostMapping("/cart/update")
    public String update(@RequestParam Long cartItemId,
                         @RequestParam int qty,
                         HttpSession session) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        cartService.updateQty(userId, cartItemId, qty);
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String remove(@RequestParam Long cartItemId, HttpSession session) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        cartService.removeItem(userId, cartItemId);
        return "redirect:/cart";
    }

    @PostMapping("/cart/clear")
    public String clear(HttpSession session) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        cartService.clearCart(userId);
        return "redirect:/cart?msg=Cart%20cleared";
    }
}