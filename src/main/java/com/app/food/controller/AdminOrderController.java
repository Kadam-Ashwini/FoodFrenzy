/*
 * package com.app.food.controller;
 * 
 * 
 * import org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.*;
 * 
 * import com.app.food.model.OrderStatus; import
 * com.app.food.service.OrderTrackingService;
 * 
 * @Controller
 * 
 * @RequestMapping("/admin/orders") public class AdminOrderController {
 * 
 * private final OrderTrackingService orderService;
 * 
 * public AdminOrderController(OrderTrackingService orderService) {
 * this.orderService = orderService; }
 * 
 * @GetMapping public String allOrders(Model model) {
 * model.addAttribute("orders", orderService.allOrders());
 * model.addAttribute("statuses", OrderStatus.values()); return "admin-orders";
 * }
 * 
 * @PostMapping("/{id}/status") public String updateStatus(@PathVariable Long
 * id, @RequestParam OrderStatus status) { orderService.adminUpdateStatus(id,
 * status); return "redirect:/admin/orders"; } }
 */
/*
 * package com.app.food.controller;
 * 
 * import java.time.LocalDate; import java.time.LocalDateTime; import
 * java.time.LocalTime; import java.util.List;
 * 
 * import org.springframework.format.annotation.DateTimeFormat; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.*;
 * 
 * import com.app.food.model.OrderStatus; import
 * com.app.food.repository.FoodOrderRepository; import
 * com.app.food.service.OrderTrackingService;
 * 
 * @Controller
 * 
 * @RequestMapping("/admin/orders") public class AdminOrderController {
 * 
 * private final OrderTrackingService orderService; private final
 * FoodOrderRepository orderRepo;
 * 
 * public AdminOrderController(OrderTrackingService orderService,
 * FoodOrderRepository orderRepo) { this.orderService = orderService;
 * this.orderRepo = orderRepo; }
 * 
 * @GetMapping public String allOrders(
 * 
 * @RequestParam(required = false) OrderStatus status,
 * 
 * @RequestParam(required = false) @DateTimeFormat(iso =
 * DateTimeFormat.ISO.DATE) LocalDate from,
 * 
 * @RequestParam(required = false) @DateTimeFormat(iso =
 * DateTimeFormat.ISO.DATE) LocalDate to, Model model) {
 * 
 * List<?> orders;
 * 
 * boolean hasDate = (from != null && to != null);
 * 
 * if (hasDate) { LocalDateTime fromDT = from.atStartOfDay(); LocalDateTime toDT
 * = to.atTime(LocalTime.MAX);
 * 
 * if (status != null) { orders =
 * orderRepo.findByStatusAndCreatedAtBetweenOrderByIdDesc(status, fromDT, toDT);
 * } else { orders = orderRepo.findByCreatedAtBetweenOrderByIdDesc(fromDT,
 * toDT); } } else { if (status != null) { orders =
 * orderRepo.findByStatusOrderByIdDesc(status); } else { orders =
 * orderRepo.findAllByOrderByIdDesc(); } }
 * 
 * model.addAttribute("orders", orders); model.addAttribute("statuses",
 * OrderStatus.values());
 * 
 * // Keep selected filters on UI model.addAttribute("selectedStatus", status);
 * model.addAttribute("from", from); model.addAttribute("to", to);
 * 
 * return "admin-orders"; }
 * 
 * @GetMapping("/{id}") public String orderDetails(@PathVariable Long id, Model
 * model) { var order = orderService.adminGetOrder(id); var items =
 * orderService.orderItems(id);
 * 
 * model.addAttribute("order", order); model.addAttribute("items", items);
 * model.addAttribute("statuses", OrderStatus.values()); return
 * "admin-order-details"; }
 * 
 * @PostMapping("/{id}/status") public String updateStatus(@PathVariable Long
 * id, @RequestParam OrderStatus status) { orderService.adminUpdateStatus(id,
 * status); return "redirect:/admin/orders/" + id + "?ok=Status%20updated"; } }
 */

/*
 * package com.app.food.controller;
 * 
 * import java.time.LocalDate; import java.time.LocalDateTime; import
 * java.time.LocalTime; import java.util.List;
 * 
 * import org.springframework.format.annotation.DateTimeFormat; import
 * org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.*;
 * 
 * import com.app.food.model.FoodOrder; import com.app.food.model.OrderStatus;
 * import com.app.food.repository.FoodOrderRepository; import
 * com.app.food.service.OrderTrackingService;
 * 
 * @Controller
 * 
 * @RequestMapping("/admin/orders") public class AdminOrderController {
 * 
 * private final OrderTrackingService orderService; private final
 * FoodOrderRepository orderRepo;
 * 
 * public AdminOrderController(OrderTrackingService orderService,
 * FoodOrderRepository orderRepo) { this.orderService = orderService;
 * this.orderRepo = orderRepo; }
 * 
 * @GetMapping public String allOrders(
 * 
 * @RequestParam(required = false) OrderStatus status,
 * 
 * @RequestParam(required = false) @DateTimeFormat(iso =
 * DateTimeFormat.ISO.DATE) LocalDate from,
 * 
 * @RequestParam(required = false) @DateTimeFormat(iso =
 * DateTimeFormat.ISO.DATE) LocalDate to, Model model) {
 * 
 * List<FoodOrder> orders; boolean hasDate = (from != null && to != null);
 * 
 * if (hasDate) { LocalDateTime fromDT = from.atStartOfDay(); LocalDateTime toDT
 * = to.atTime(LocalTime.MAX);
 * 
 * if (status != null) { orders =
 * orderRepo.findByStatusAndCreatedAtBetweenOrderByIdDesc(status, fromDT, toDT);
 * } else { orders = orderRepo.findByCreatedAtBetweenOrderByIdDesc(fromDT,
 * toDT); } } else { if (status != null) { orders =
 * orderRepo.findByStatusOrderByIdDesc(status); } else { orders =
 * orderRepo.findAllByOrderByIdDesc(); } }
 * 
 * model.addAttribute("orders", orders); model.addAttribute("statuses",
 * OrderStatus.values());
 * 
 * model.addAttribute("selectedStatus", status); model.addAttribute("from",
 * from); model.addAttribute("to", to);
 * 
 * return "admin-orders"; }
 * 
 * @GetMapping("/{id}") public String orderDetails(@PathVariable Long id, Model
 * model) { var order = orderService.adminGetOrder(id); var items =
 * orderService.orderItems(id);
 * 
 * model.addAttribute("order", order); model.addAttribute("items", items);
 * model.addAttribute("statuses", OrderStatus.values()); return
 * "admin-order-details"; }
 * 
 * @PostMapping("/{id}/status") public String updateStatus(@PathVariable Long
 * id, @RequestParam OrderStatus status) { try {
 * orderService.adminUpdateStatus(id, status); return "redirect:/admin/orders/"
 * + id + "?ok=Status%20updated"; } catch (Exception e) { String msg =
 * (e.getMessage() == null ? "Error" : e.getMessage()).replace(" ", "%20");
 * return "redirect:/admin/orders/" + id + "?msg=" + msg; } } }
 */



package com.app.food.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.app.food.model.FoodOrder;
import com.app.food.model.OrderStatus;
import com.app.food.repository.FoodOrderRepository;
import com.app.food.service.DeliveryService;
import com.app.food.service.OrderTrackingService;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private final OrderTrackingService orderService;
    private final FoodOrderRepository orderRepo;
    private final DeliveryService deliveryService;

    public AdminOrderController(OrderTrackingService orderService,
                                FoodOrderRepository orderRepo,
                                DeliveryService deliveryService) {
        this.orderService = orderService;
        this.orderRepo = orderRepo;
        this.deliveryService = deliveryService;
    }

    @GetMapping
    public String allOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Model model) {

        List<FoodOrder> orders;
        boolean hasDate = (from != null && to != null);

        if (hasDate) {
            LocalDateTime fromDT = from.atStartOfDay();
            LocalDateTime toDT = to.atTime(LocalTime.MAX);

            if (status != null) {
                orders = orderRepo.findByStatusAndCreatedAtBetweenOrderByIdDesc(status, fromDT, toDT);
            } else {
                orders = orderRepo.findByCreatedAtBetweenOrderByIdDesc(fromDT, toDT);
            }
        } else {
            if (status != null) {
                orders = orderRepo.findByStatusOrderByIdDesc(status);
            } else {
                orders = orderRepo.findAllByOrderByIdDesc();
            }
        }

        model.addAttribute("orders", orders);
        model.addAttribute("statuses", OrderStatus.values());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("from", from);
        model.addAttribute("to", to);

        return "admin-orders";
    }

    @GetMapping("/{id}")
    public String orderDetails(@PathVariable Long id, Model model) {
        var order = orderService.adminGetOrder(id);
        var items = orderService.orderItems(id);

        // ✅ delivery boy data for dropdown + assigned agent
        model.addAttribute("agents", deliveryService.activeAgents());
        model.addAttribute("assignment", deliveryService.assignmentOrNull(id));

        model.addAttribute("order", order);
        model.addAttribute("items", items);
        model.addAttribute("statuses", OrderStatus.values());
        return "admin-order-details";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        try {
            orderService.adminUpdateStatus(id, status);
            return "redirect:/admin/orders/" + id + "?ok=Status%20updated";
        } catch (Exception e) {
            String msg = (e.getMessage() == null ? "Error" : e.getMessage()).replace(" ", "%20");
            return "redirect:/admin/orders/" + id + "?msg=" + msg;
        }
    }

    // ✅ NEW: assign delivery boy and move order to OUT_FOR_DELIVERY
    @PostMapping("/{id}/assign-delivery")
    public String assignDelivery(@PathVariable Long id, @RequestParam Long agentId) {
        try {
            deliveryService.assignAgentAndStartDelivery(id, agentId);
            return "redirect:/admin/orders/" + id + "?ok=Delivery%20boy%20assigned%20and%20order%20out%20for%20delivery";
        } catch (Exception e) {
            String msg = (e.getMessage() == null ? "Error" : e.getMessage()).replace(" ", "%20");
            return "redirect:/admin/orders/" + id + "?msg=" + msg;
        }
    }
}