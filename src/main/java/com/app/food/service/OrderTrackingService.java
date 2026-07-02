/*
 * package com.app.food.service;
 * 
 * 
 * 
 * import java.util.List;
 * 
 * import org.springframework.stereotype.Service; import
 * org.springframework.transaction.annotation.Transactional;
 * 
 * import com.app.food.model.FoodOrder; import com.app.food.model.OrderItem;
 * import com.app.food.model.OrderStatus; import
 * com.app.food.repository.FoodOrderRepository; import
 * com.app.food.repository.OrderItemRepository;
 * 
 * @Service public class OrderTrackingService {
 * 
 * private final FoodOrderRepository orderRepo; private final
 * OrderItemRepository orderItemRepo;
 * 
 * public OrderTrackingService(FoodOrderRepository orderRepo,
 * OrderItemRepository orderItemRepo) { this.orderRepo = orderRepo;
 * this.orderItemRepo = orderItemRepo; }
 * 
 * public List<FoodOrder> myOrders(Long userId) { return
 * orderRepo.findByUserIdOrderByIdDesc(userId); }
 * 
 * public FoodOrder getOrderForUser(Long orderId, Long userId) { return
 * orderRepo.findByIdAndUserId(orderId, userId) .orElseThrow(() -> new
 * RuntimeException("Order not found")); }
 * 
 * public List<OrderItem> orderItems(Long orderId) { return
 * orderItemRepo.findByOrderId(orderId); }
 * 
 * @Transactional public void cancelOrder(Long orderId, Long userId) { FoodOrder
 * order = getOrderForUser(orderId, userId);
 * 
 * // Cancel allowed only BEFORE confirmation if (order.getStatus() !=
 * OrderStatus.CREATED) { throw new
 * RuntimeException("Cannot cancel. Order already confirmed/processed."); }
 * 
 * order.setStatus(OrderStatus.CANCELLED); orderRepo.save(order); }
 * 
 * // Admin public List<FoodOrder> allOrders() { return
 * orderRepo.findAllByOrderByIdDesc(); }
 * 
 * @Transactional public void adminUpdateStatus(Long orderId, OrderStatus
 * newStatus) { FoodOrder order = orderRepo.findById(orderId).orElseThrow(() ->
 * new RuntimeException("Order not found"));
 * 
 * // Simple rule: once cancelled/delivered, do not change if (order.getStatus()
 * == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.DELIVERED) {
 * throw new
 * RuntimeException("Cannot change status for CANCELLED/DELIVERED orders."); }
 * 
 * order.setStatus(newStatus); orderRepo.save(order); }
 * 
 * 
 * 
 * 
 * public FoodOrder adminGetOrder(Long orderId) { return
 * orderRepo.findById(orderId).orElseThrow(() -> new
 * RuntimeException("Order not found")); } }
 */

/*
 * 
 * package com.app.food.service; import java.util.*; import
 * java.util.stream.Collectors; import java.util.List;
 * 
 * import org.springframework.stereotype.Service; import
 * org.springframework.transaction.annotation.Transactional;
 * 
 * import com.app.food.model.FoodOrder; import com.app.food.model.OrderItem;
 * import com.app.food.model.OrderStatus; import
 * com.app.food.repository.FoodOrderRepository; import
 * com.app.food.repository.OrderItemRepository;
 * 
 * @Service public class OrderTrackingService {
 * 
 * private final FoodOrderRepository orderRepo; private final
 * OrderItemRepository orderItemRepo;
 * 
 * public OrderTrackingService(FoodOrderRepository orderRepo,
 * OrderItemRepository orderItemRepo) { this.orderRepo = orderRepo;
 * this.orderItemRepo = orderItemRepo; }
 * 
 * // User public List<FoodOrder> myOrders(Long userId) { return
 * orderRepo.findByUserIdOrderByIdDesc(userId); }
 * 
 * public FoodOrder getOrderForUser(Long orderId, Long userId) { return
 * orderRepo.findByIdAndUserId(orderId, userId) .orElseThrow(() -> new
 * RuntimeException("Order not found")); }
 * 
 * public List<OrderItem> orderItems(Long orderId) { return
 * orderItemRepo.findByOrderId(orderId); }
 * 
 * @Transactional public void cancelOrder(Long orderId, Long userId) { FoodOrder
 * order = getOrderForUser(orderId, userId);
 * 
 * if (order.getStatus() != OrderStatus.CREATED) { throw new
 * RuntimeException("Cannot cancel. Order already confirmed/processed."); }
 * 
 * order.setStatus(OrderStatus.CANCELLED); orderRepo.save(order); }
 * 
 * // Admin public List<FoodOrder> allOrders() { return
 * orderRepo.findAllByOrderByIdDesc(); }
 * 
 * public FoodOrder adminGetOrder(Long orderId) { return
 * orderRepo.findById(orderId).orElseThrow(() -> new
 * RuntimeException("Order not found")); }
 * 
 * @Transactional public void adminUpdateStatus(Long orderId, OrderStatus
 * newStatus) { FoodOrder order = orderRepo.findById(orderId) .orElseThrow(() ->
 * new RuntimeException("Order not found"));
 * 
 * // CANCELLED/DELIVERED => no status change allowed if (order.getStatus() ==
 * OrderStatus.CANCELLED || order.getStatus() == OrderStatus.DELIVERED) { //
 * Allow re-saving same status without error if (order.getStatus() == newStatus)
 * return;
 * 
 * throw new
 * RuntimeException("Cannot change status for CANCELLED/DELIVERED orders."); }
 * 
 * order.setStatus(newStatus); orderRepo.save(order); }
 * 
 * 
 * @Transactional public void userConfirmDelivered(Long orderId, Long userId) {
 * FoodOrder order = getOrderForUser(orderId, userId);
 * 
 * if (order.getStatus() != OrderStatus.OUT_FOR_DELIVERY) { throw new
 * RuntimeException("You can confirm only when order is OUT_FOR_DELIVERY."); }
 * 
 * order.setStatus(OrderStatus.DELIVERED); orderRepo.save(order); }
 * 
 * 
 * 
 * 
 * 
 * public Map<Long, List<OrderItem>> orderItemsForOrders(List<Long> orderIds) {
 * if (orderIds == null || orderIds.isEmpty()) return Map.of();
 * 
 * List<OrderItem> items = orderItemRepo.findByOrderIdIn(orderIds);
 * 
 * return items.stream().collect(Collectors.groupingBy(oi ->
 * oi.getOrder().getId())); } }
 */



// FILE: src/main/java/com/app/food/service/OrderTrackingService.java
package com.app.food.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.food.model.FoodOrder;
import com.app.food.model.OrderItem;
import com.app.food.model.OrderStatus;
import com.app.food.repository.FoodOrderRepository;
import com.app.food.repository.OrderItemRepository;

@Service
public class OrderTrackingService {

    private final FoodOrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;

    public OrderTrackingService(FoodOrderRepository orderRepo, OrderItemRepository orderItemRepo) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
    }

    // User
    public List<FoodOrder> myOrders(Long userId) {
        return orderRepo.findByUserIdOrderByIdDesc(userId);
    }

    public FoodOrder getOrderForUser(Long orderId, Long userId) {
        return orderRepo.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<OrderItem> orderItems(Long orderId) {
        return orderItemRepo.findByOrderId(orderId);
    }

    @Transactional
    public void cancelOrder(Long orderId, Long userId) {
        FoodOrder order = getOrderForUser(orderId, userId);

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new RuntimeException("Cannot cancel. Order already confirmed/processed.");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepo.save(order);
    }

    // Admin
    public List<FoodOrder> allOrders() {
        return orderRepo.findAllByOrderByIdDesc();
    }

    public FoodOrder adminGetOrder(Long orderId) {
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    public void adminUpdateStatus(Long orderId, OrderStatus newStatus) {
        FoodOrder order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.DELIVERED) {
            if (order.getStatus() == newStatus) return;
            throw new RuntimeException("Cannot change status for CANCELLED/DELIVERED orders.");
        }

        order.setStatus(newStatus);
        orderRepo.save(order);
    }

    @Transactional
    public void userConfirmDelivered(Long orderId, Long userId) {
        FoodOrder order = getOrderForUser(orderId, userId);

        if (order.getStatus() != OrderStatus.OUT_FOR_DELIVERY) {
            throw new RuntimeException("You can confirm only when order is OUT_FOR_DELIVERY.");
        }

        order.setStatus(OrderStatus.DELIVERED);
        orderRepo.save(order);
    }

    // ✅ For My Orders UI: orderId -> items list
    public Map<Long, List<OrderItem>> orderItemsForOrders(List<Long> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) return Map.of();

        // Fetch with join fetch to safely read oi.getOrder().getId()
        List<OrderItem> items = orderItemRepo.findByOrderIdsFetchOrder(orderIds);

        return items.stream()
                .collect(Collectors.groupingBy(oi -> oi.getOrder().getId()));
    }
}