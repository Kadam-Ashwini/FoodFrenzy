/*
 * package com.app.food.service;
 * 
 * 
 * import java.math.BigDecimal; import java.time.LocalDate; import
 * java.time.LocalDateTime; import java.time.LocalTime; import
 * java.util.EnumMap; import java.util.List; import java.util.Map;
 * 
 * import org.springframework.stereotype.Service;
 * 
 * import com.app.food.model.FoodOrder; import com.app.food.model.OrderStatus;
 * import com.app.food.model.PaymentStatus; import
 * com.app.food.repository.FoodOrderRepository;
 * 
 * @Service public class AdminReportService {
 * 
 * private final FoodOrderRepository orderRepo;
 * 
 * public AdminReportService(FoodOrderRepository orderRepo) { this.orderRepo =
 * orderRepo; }
 * 
 * public long totalOrders() { return orderRepo.count(); }
 * 
 * public BigDecimal totalRevenuePaid() { List<FoodOrder> all =
 * orderRepo.findAll(); return all.stream() .filter(o -> o.getPaymentStatus() ==
 * PaymentStatus.PAID) .map(FoodOrder::getTotalAmount) .reduce(BigDecimal.ZERO,
 * BigDecimal::add); }
 * 
 * public Map<OrderStatus, Long> ordersByStatus() { List<FoodOrder> all =
 * orderRepo.findAll(); Map<OrderStatus, Long> map = new
 * EnumMap<>(OrderStatus.class);
 * 
 * for (OrderStatus s : OrderStatus.values()) map.put(s, 0L); for (FoodOrder o :
 * all) map.put(o.getStatus(), map.get(o.getStatus()) + 1);
 * 
 * return map; }
 * 
 * public long todayOrders() { LocalDateTime from =
 * LocalDate.now().atStartOfDay(); LocalDateTime to =
 * LocalDate.now().atTime(LocalTime.MAX); return
 * orderRepo.findByCreatedAtBetweenOrderByIdDesc(from, to).size(); }
 * 
 * public BigDecimal todayRevenuePaid() { LocalDateTime from =
 * LocalDate.now().atStartOfDay(); LocalDateTime to =
 * LocalDate.now().atTime(LocalTime.MAX);
 * 
 * return orderRepo.findByCreatedAtBetweenOrderByIdDesc(from, to).stream()
 * .filter(o -> o.getPaymentStatus() == PaymentStatus.PAID)
 * .map(FoodOrder::getTotalAmount) .reduce(BigDecimal.ZERO, BigDecimal::add); }
 * }
 */

package com.app.food.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.app.food.model.FoodOrder;
import com.app.food.model.OrderStatus;
import com.app.food.model.PaymentStatus;
import com.app.food.repository.FoodOrderRepository;
import com.app.food.repository.OrderItemRepository;
import com.app.food.repository.projection.TopFoodItemRow;

@Service
public class AdminReportService {

    private final FoodOrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;

    public AdminReportService(FoodOrderRepository orderRepo, OrderItemRepository orderItemRepo) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
    }

    // ----- Existing (Dashboard) -----
    public long totalOrders() {
        return orderRepo.count();
    }

    public BigDecimal totalRevenuePaid() {
        List<FoodOrder> all = orderRepo.findAll();
        return all.stream()
                .filter(o -> o.getPaymentStatus() == PaymentStatus.PAID)
                .map(FoodOrder::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<OrderStatus, Long> ordersByStatus() {
        List<FoodOrder> all = orderRepo.findAll();
        Map<OrderStatus, Long> map = new EnumMap<>(OrderStatus.class);

        for (OrderStatus s : OrderStatus.values()) map.put(s, 0L);
        for (FoodOrder o : all) map.put(o.getStatus(), map.get(o.getStatus()) + 1);

        return map;
    }

    public long todayOrders() {
        LocalDateTime from = LocalDate.now().atStartOfDay();
        LocalDateTime to = LocalDate.now().atTime(LocalTime.MAX);
        return orderRepo.findByCreatedAtBetweenOrderByIdDesc(from, to).size();
    }

    public BigDecimal todayRevenuePaid() {
        LocalDateTime from = LocalDate.now().atStartOfDay();
        LocalDateTime to = LocalDate.now().atTime(LocalTime.MAX);

        return orderRepo.findByPaymentStatusAndCreatedAtBetweenOrderByIdDesc(PaymentStatus.PAID, from, to).stream()
                .map(FoodOrder::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ----- New (Step 10.2) -----
    public long ordersCountBetween(LocalDate fromDate, LocalDate toDate) {
        LocalDateTime from = fromDate.atStartOfDay();
        LocalDateTime to = toDate.atTime(LocalTime.MAX);
        return orderRepo.findByCreatedAtBetweenOrderByIdDesc(from, to).size();
    }

    public BigDecimal paidRevenueBetween(LocalDate fromDate, LocalDate toDate) {
        LocalDateTime from = fromDate.atStartOfDay();
        LocalDateTime to = toDate.atTime(LocalTime.MAX);

        return orderRepo.findByPaymentStatusAndCreatedAtBetweenOrderByIdDesc(PaymentStatus.PAID, from, to).stream()
                .map(FoodOrder::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<TopFoodItemRow> topItemsBetween(LocalDate fromDate, LocalDate toDate) {
        LocalDateTime from = fromDate.atStartOfDay();
        LocalDateTime to = toDate.atTime(LocalTime.MAX);
        return orderItemRepo.topItemsPaidBetween(PaymentStatus.PAID, from, to);
    }
}