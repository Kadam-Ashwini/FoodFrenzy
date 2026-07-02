/*
 * package com.app.food.controller;
 * 
 * 
 * import org.springframework.stereotype.Controller; import
 * org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.RequestMapping;
 * 
 * import com.app.food.service.AdminReportService;
 * 
 * @Controller
 * 
 * @RequestMapping("/admin/reports") public class AdminReportController {
 * 
 * private final AdminReportService reportService;
 * 
 * public AdminReportController(AdminReportService reportService) {
 * this.reportService = reportService; }
 * 
 * @GetMapping("/dashboard") public String dashboard(Model model) {
 * model.addAttribute("totalOrders", reportService.totalOrders());
 * model.addAttribute("totalRevenuePaid", reportService.totalRevenuePaid());
 * model.addAttribute("todayOrders", reportService.todayOrders());
 * model.addAttribute("todayRevenuePaid", reportService.todayRevenuePaid());
 * model.addAttribute("ordersByStatus", reportService.ordersByStatus()); return
 * "admin-report-dashboard"; } }
 */


package com.app.food.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.app.food.service.AdminReportService;

@Controller
@RequestMapping("/admin/reports")
public class AdminReportController {

    private final AdminReportService reportService;

    public AdminReportController(AdminReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalOrders", reportService.totalOrders());
        model.addAttribute("totalRevenuePaid", reportService.totalRevenuePaid());
        model.addAttribute("todayOrders", reportService.todayOrders());
        model.addAttribute("todayRevenuePaid", reportService.todayRevenuePaid());
        model.addAttribute("ordersByStatus", reportService.ordersByStatus());
        return "admin-report-dashboard";
    }

    @GetMapping("/top-items")
    public String topItems(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Model model) {

        // default: last 7 days
        if (to == null) to = LocalDate.now();
        if (from == null) from = to.minusDays(6);

        model.addAttribute("from", from);
        model.addAttribute("to", to);

        model.addAttribute("ordersCount", reportService.ordersCountBetween(from, to));
        model.addAttribute("paidRevenue", reportService.paidRevenueBetween(from, to));
        model.addAttribute("topItems", reportService.topItemsBetween(from, to));

        return "admin-report-top-items";
    }
}