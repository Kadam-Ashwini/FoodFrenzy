/*
 * package com.app.food.controller;
 * 
 * import java.util.List;
 * 
 * import org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.*;
 * 
 * import com.app.food.model.FoodOrder; import com.app.food.model.OrderItem;
 * import com.app.food.model.OrderStatus; import
 * com.app.food.service.OrderTrackingService;
 * 
 * import jakarta.servlet.http.HttpSession;
 * 
 * @Controller public class OrderController {
 * 
 * private final OrderTrackingService orderService;
 * 
 * public OrderController(OrderTrackingService orderService) { this.orderService
 * = orderService; }
 * 
 * private Long requireUserId(HttpSession session) { Object uid =
 * session.getAttribute("userId"); return uid == null ? null : (Long) uid; }
 * 
 * 
 * @GetMapping("/orders") public String myOrders(HttpSession session, Model
 * model) { Long userId = requireUserId(session); if (userId == null) return
 * "redirect:/login";
 * 
 * List<FoodOrder> orders = orderService.myOrders(userId);
 * model.addAttribute("orders", orders); return "orders"; }
 * 
 * 
 * 
 * 
 * 
 * @GetMapping("/orders") public String myOrders(HttpSession session, Model
 * model) { Object uid = session.getAttribute("userId"); Long userId = (uid
 * instanceof Number) ? ((Number) uid).longValue() : null; if (userId == null)
 * return "redirect:/login";
 * 
 * List<FoodOrder> orders = orderService.myOrders(userId);
 * 
 * // Collect orderIds List<Long> orderIds =
 * orders.stream().map(FoodOrder::getId).toList();
 * 
 * // Fetch all items in one go and group by orderId var allItems =
 * orderService.orderItemsForOrders(orderIds);
 * 
 * model.addAttribute("orders", orders); model.addAttribute("itemsByOrder",
 * allItems); return "orders"; }
 * 
 * 
 * 
 * @GetMapping("/orders/{id}") public String orderDetails(@PathVariable Long id,
 * HttpSession session, Model model) { Long userId = requireUserId(session); if
 * (userId == null) return "redirect:/login";
 * 
 * FoodOrder order = orderService.getOrderForUser(id, userId); List<OrderItem>
 * items = orderService.orderItems(id);
 * 
 * model.addAttribute("order", order); model.addAttribute("items", items);
 * model.addAttribute("allStatuses", OrderStatus.values()); return
 * "order-details"; }
 * 
 * @PostMapping("/orders/{id}/cancel") public String cancel(@PathVariable Long
 * id, HttpSession session) { Long userId = requireUserId(session); if (userId
 * == null) return "redirect:/login";
 * 
 * orderService.cancelOrder(id, userId); return "redirect:/orders/" + id; } }
 */





// FILE: src/main/java/com/app/food/controller/OrderController.java
package com.app.food.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.app.food.model.FoodOrder;
import com.app.food.model.OrderItem;
import com.app.food.model.OrderStatus;
import com.app.food.service.OrderTrackingService;

import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {

    private final OrderTrackingService orderService;

    public OrderController(OrderTrackingService orderService) {
        this.orderService = orderService;
    }

    private Long requireUserId(HttpSession session) {
        Object uid = session.getAttribute("userId");
        return (uid instanceof Number) ? ((Number) uid).longValue() : null;
    }

    @GetMapping("/orders")
    public String myOrders(HttpSession session, Model model) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        List<FoodOrder> orders = orderService.myOrders(userId);
        List<Long> orderIds = orders.stream().map(FoodOrder::getId).toList();

        model.addAttribute("orders", orders);
        model.addAttribute("itemsByOrder", orderService.orderItemsForOrders(orderIds));
        return "orders";
    }

    @GetMapping("/orders/{id}")
    public String orderDetails(@PathVariable Long id, HttpSession session, Model model) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        FoodOrder order = orderService.getOrderForUser(id, userId);
        List<OrderItem> items = orderService.orderItems(id);

        model.addAttribute("order", order);
        model.addAttribute("items", items);
        model.addAttribute("allStatuses", OrderStatus.values());
        return "order-details";
    }

    @PostMapping("/orders/{id}/cancel")
    public String cancel(@PathVariable Long id, HttpSession session) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        orderService.cancelOrder(id, userId);
        return "redirect:/orders/" + id;
    }
}