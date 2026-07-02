/*
 * package com.app.food.controller;
 * 
 * 
 * import org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.*;
 * 
 * import com.app.food.repository.RestaurantRepository; import
 * com.app.food.service.GroupOrderService;
 * 
 * import jakarta.servlet.http.HttpSession;
 * 
 * @Controller public class GroupOrderController {
 * 
 * private final GroupOrderService groupService; private final
 * RestaurantRepository restaurantRepo;
 * 
 * public GroupOrderController(GroupOrderService groupService,
 * RestaurantRepository restaurantRepo) { this.groupService = groupService;
 * this.restaurantRepo = restaurantRepo; }
 * 
 * private Long requireUserId(HttpSession session) { Object uid =
 * session.getAttribute("userId"); return uid == null ? null : (Long) uid; }
 * 
 * @GetMapping("/group") public String groupHome(HttpSession session, Model
 * model) { Long userId = requireUserId(session); if (userId == null) return
 * "redirect:/login";
 * 
 * model.addAttribute("restaurants",
 * restaurantRepo.findByActiveTrueOrderByIdDesc()); return "group-home"; }
 * 
 * @PostMapping("/group/create") public String create(@RequestParam Long
 * restaurantId, HttpSession session) { Long userId = requireUserId(session); if
 * (userId == null) return "redirect:/login";
 * 
 * var g = groupService.createGroup(userId, restaurantId); return
 * "redirect:/group/" + g.getCode(); }
 * 
 * @PostMapping("/group/join") public String join(@RequestParam String code,
 * HttpSession session) { Long userId = requireUserId(session); if (userId ==
 * null) return "redirect:/login";
 * 
 * var g = groupService.joinGroup(userId, code); return "redirect:/group/" +
 * g.getCode(); }
 * 
 * @GetMapping("/group/{code}") public String view(@PathVariable String code,
 * HttpSession session, Model model) { Long userId = requireUserId(session); if
 * (userId == null) return "redirect:/login";
 * 
 * var g = groupService.getByCode(code); model.addAttribute("group", g);
 * model.addAttribute("members", groupService.members(g.getId()));
 * model.addAttribute("items", groupService.items(g.getId())); return
 * "group-order"; }
 * 
 * @PostMapping("/group/{code}/add") public String add(@PathVariable String
 * code,
 * 
 * @RequestParam Long foodItemId,
 * 
 * @RequestParam Long restaurantId, HttpSession session) { Long userId =
 * requireUserId(session); if (userId == null) return "redirect:/login";
 * 
 * try { groupService.addItem(userId, code, foodItemId); return
 * "redirect:/restaurants/" + restaurantId + "?groupCode=" + code +
 * "&ok=Added%20to%20group%20order"; } catch (Exception e) { String msg =
 * (e.getMessage() == null ? "Error" : e.getMessage()).replace(" ", "%20");
 * return "redirect:/restaurants/" + restaurantId + "?groupCode=" + code +
 * "&msg=" + msg; } } }
 */



package com.app.food.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.app.food.repository.RestaurantRepository;
import com.app.food.service.GroupOrderService;

import jakarta.servlet.http.HttpSession;

@Controller
public class GroupOrderController {

    private final GroupOrderService groupService;
    private final RestaurantRepository restaurantRepo;

    public GroupOrderController(GroupOrderService groupService, RestaurantRepository restaurantRepo) {
        this.groupService = groupService;
        this.restaurantRepo = restaurantRepo;
    }

    private Long requireUserId(HttpSession session) {
        Object uid = session.getAttribute("userId");
        return uid == null ? null : (Long) uid;
    }

    @GetMapping("/group")
    public String groupHome(HttpSession session, Model model) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        model.addAttribute("restaurants", restaurantRepo.findByActiveTrueOrderByIdDesc());
        return "group-home";
    }

    @PostMapping("/group/create")
    public String create(@RequestParam Long restaurantId, HttpSession session) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        var g = groupService.createGroup(userId, restaurantId);
        return "redirect:/group/" + g.getCode();
    }

    @PostMapping("/group/join")
    public String join(@RequestParam String code, HttpSession session) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        var g = groupService.joinGroup(userId, code);
        return "redirect:/group/" + g.getCode();
    }

    @GetMapping("/group/{code}")
    public String view(@PathVariable String code, HttpSession session, Model model) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        var g = groupService.getByCode(code);

        model.addAttribute("group", g);
        model.addAttribute("members", groupService.members(g.getId()));
        model.addAttribute("items", groupService.items(g.getId()));
        model.addAttribute("total", groupService.groupTotal(g.getId()));
        model.addAttribute("isHost", g.getHost().getId().equals(userId));
        return "group-order";
    }

    @PostMapping("/group/{code}/add")
    public String add(@PathVariable String code,
                      @RequestParam Long foodItemId,
                      @RequestParam Long restaurantId,
                      HttpSession session) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        try {
            groupService.addItem(userId, code, foodItemId);
            return "redirect:/restaurants/" + restaurantId + "?groupCode=" + code + "&ok=Added%20to%20group%20order";
        } catch (Exception e) {
            String msg = (e.getMessage() == null ? "Error" : e.getMessage()).replace(" ", "%20");
            return "redirect:/restaurants/" + restaurantId + "?groupCode=" + code + "&msg=" + msg;
        }
    }

    // ---------- Finalize (Host only) ----------
    @GetMapping("/group/{code}/finalize")
    public String finalizePage(@PathVariable String code, HttpSession session, Model model) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        var g = groupService.getByCode(code);

        if (!g.getHost().getId().equals(userId)) {
            return "redirect:/group/" + code + "?msg=Only%20host%20can%20finalize";
        }

        model.addAttribute("group", g);
        model.addAttribute("items", groupService.items(g.getId()));
        model.addAttribute("total", groupService.groupTotal(g.getId()));
//        model.addAttribute("cities", List.of("Hyderabad", "Bangalore", "Chennai", "Mumbai", "Delhi"));
        
        model.addAttribute("cities", List.of("Nashik", "Pune", "Nagpur", "Mumbai", "Delhi","Hyderabad", "Bangalore", "Chennai"));
        
        return "group-finalize";
    }

    @PostMapping("/group/{code}/finalize")
    public String doFinalize(@PathVariable String code,
                             @RequestParam String deliveryAddress,
                             @RequestParam String city,
                             HttpSession session) {
        Long userId = requireUserId(session);
        if (userId == null) return "redirect:/login";

        try {
            Long orderId = groupService.finalizeGroup(userId, code, deliveryAddress.trim(), city);
            return "redirect:/payment/" + orderId;
        } catch (Exception e) {
            String msg = (e.getMessage() == null ? "Error" : e.getMessage()).replace(" ", "%20");
            return "redirect:/group/" + code + "?msg=" + msg;
        }
    }
}