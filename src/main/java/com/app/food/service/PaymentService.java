/*
 * package com.app.food.service; import java.util.List; import java.util.UUID;
 * 
 * import org.springframework.stereotype.Service; import
 * org.springframework.transaction.annotation.Transactional;
 * 
 * import com.app.food.model.FoodOrder; import com.app.food.model.OrderItem;
 * import com.app.food.model.PaymentMethod; import
 * com.app.food.model.PaymentStatus; import
 * com.app.food.repository.FoodOrderRepository; import
 * com.app.food.repository.OrderItemRepository;
 * 
 * @Service public class PaymentService {
 * 
 * private final FoodOrderRepository orderRepo; private final
 * OrderItemRepository orderItemRepo;
 * 
 * public PaymentService(FoodOrderRepository orderRepo, OrderItemRepository
 * orderItemRepo) { this.orderRepo = orderRepo; this.orderItemRepo =
 * orderItemRepo; }
 * 
 * public FoodOrder getOrderForUser(Long orderId, Long userId) { return
 * orderRepo.findByIdAndUserId(orderId, userId) .orElseThrow(() -> new
 * RuntimeException("Order not found")); }
 * 
 * public List<OrderItem> items(Long orderId) { return
 * orderItemRepo.findByOrderId(orderId); }
 * 
 * @Transactional public void markPaid(Long orderId, Long userId, PaymentMethod
 * method) { FoodOrder order = getOrderForUser(orderId, userId);
 * 
 * order.setPaymentMethod(method); order.setPaymentStatus(PaymentStatus.PAID);
 * order.setPaymentRef("PAY-" + UUID.randomUUID().toString().substring(0,
 * 10).toUpperCase());
 * 
 * orderRepo.save(order); }
 * 
 * @Transactional public void markFailed(Long orderId, Long userId,
 * PaymentMethod method) { FoodOrder order = getOrderForUser(orderId, userId);
 * 
 * order.setPaymentMethod(method); order.setPaymentStatus(PaymentStatus.FAILED);
 * order.setPaymentRef(null);
 * 
 * orderRepo.save(order); }
 * 
 * @Transactional public void chooseCod(Long orderId, Long userId) { FoodOrder
 * order = getOrderForUser(orderId, userId);
 * 
 * order.setPaymentMethod(PaymentMethod.COD);
 * order.setPaymentStatus(PaymentStatus.PENDING); order.setPaymentRef(null);
 * 
 * orderRepo.save(order); } }
 */


package com.app.food.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.food.model.FoodOrder;
import com.app.food.model.OrderItem;
import com.app.food.model.OrderStatus;
import com.app.food.model.PaymentMethod;
import com.app.food.model.PaymentStatus;
import com.app.food.model.PaymentTransaction;
import com.app.food.repository.FoodOrderRepository;
import com.app.food.repository.OrderItemRepository;
import com.app.food.repository.PaymentTransactionRepository;

@Service
public class PaymentService {

    private final FoodOrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final PaymentTransactionRepository txRepo;

    public PaymentService(FoodOrderRepository orderRepo,
                          OrderItemRepository orderItemRepo,
                          PaymentTransactionRepository txRepo) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.txRepo = txRepo;
    }

    public FoodOrder getOrderForUser(Long orderId, Long userId) {
        return orderRepo.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<OrderItem> items(Long orderId) {
        return orderItemRepo.findByOrderId(orderId);
    }

    public List<PaymentTransaction> transactions(Long orderId) {
        return txRepo.findByOrderIdOrderByIdDesc(orderId);
    }

    private void validatePayable(FoodOrder order) {
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Order is cancelled. Payment not allowed.");
        }
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Order already delivered. Payment not allowed.");
        }
    }

    @Transactional
    public void markPaid(Long orderId, Long userId, PaymentMethod method) {
        FoodOrder order = getOrderForUser(orderId, userId);
        validatePayable(order);

        // Update order payment fields
        order.setPaymentMethod(method);
        order.setPaymentStatus(PaymentStatus.PAID);
        order.setPaymentRef("PAY-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        orderRepo.save(order);

        // Insert transaction row
        PaymentTransaction tx = new PaymentTransaction();
        tx.setOrder(order);
        tx.setMethod(method);
        tx.setStatus(PaymentStatus.PAID);
        tx.setAmount(order.getTotalAmount());
        tx.setRef(order.getPaymentRef());
        tx.setMessage("Mock payment success");
        txRepo.save(tx);
    }

    @Transactional
    public void markFailed(Long orderId, Long userId, PaymentMethod method) {
        FoodOrder order = getOrderForUser(orderId, userId);
        validatePayable(order);

        // Update order payment fields
        order.setPaymentMethod(method);
        order.setPaymentStatus(PaymentStatus.FAILED);
        order.setPaymentRef(null);
        orderRepo.save(order);

        // Insert transaction row
        PaymentTransaction tx = new PaymentTransaction();
        tx.setOrder(order);
        tx.setMethod(method);
        tx.setStatus(PaymentStatus.FAILED);
        tx.setAmount(order.getTotalAmount());
        tx.setRef(null);
        tx.setMessage("Mock payment failed");
        txRepo.save(tx);
    }

    @Transactional
    public void chooseCod(Long orderId, Long userId) {
        FoodOrder order = getOrderForUser(orderId, userId);
        validatePayable(order);

        // Order stays pending (COD)
        order.setPaymentMethod(PaymentMethod.COD);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setPaymentRef(null);
        orderRepo.save(order);

        // Insert transaction row (pending)
        PaymentTransaction tx = new PaymentTransaction();
        tx.setOrder(order);
        tx.setMethod(PaymentMethod.COD);
        tx.setStatus(PaymentStatus.PENDING);
        tx.setAmount(order.getTotalAmount());
        tx.setRef(null);
        tx.setMessage("Cash on Delivery selected");
        txRepo.save(tx);
    }
}

