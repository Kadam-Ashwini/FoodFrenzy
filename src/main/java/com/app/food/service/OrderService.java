package com.app.food.service;



import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.food.model.*;
import com.app.food.repository.*;

@Service
public class OrderService {

    private final AppUserRepository userRepo;
    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final FoodOrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;

    public OrderService(AppUserRepository userRepo,
                        CartRepository cartRepo,
                        CartItemRepository cartItemRepo,
                        FoodOrderRepository orderRepo,
                        OrderItemRepository orderItemRepo) {
        this.userRepo = userRepo;
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
    }

    @Transactional
    public Long placeOrder(Long userId, String deliveryAddress, String city) {

        AppUser user = userRepo.findById(userId).orElseThrow();

        Cart cart = cartRepo.findByUserIdAndStatus(userId, CartStatus.OPEN)
                .orElseThrow(() -> new RuntimeException("Cart is empty"));

        List<CartItem> cartItems = cartItemRepo.findByCartId(cart.getId());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // total
        BigDecimal total = cartItems.stream()
                .map(CartItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // create order
        FoodOrder order = new FoodOrder();
        order.setUser(user);
        order.setRestaurant(cart.getRestaurant());
        order.setStatus(OrderStatus.CREATED);
        order.setPaymentStatus(PaymentStatus.PENDING); // payment module later
        order.setTotalAmount(total);
        order.setDeliveryAddress(deliveryAddress);
        order.setCity(city);

        order = orderRepo.save(order);

        // create order items (snapshot)
        for (CartItem ci : cartItems) {
            FoodItem f = ci.getFoodItem();

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setFoodName(f.getName());
            oi.setCategory(f.getCategory());
            oi.setVeg(f.isVeg());
            oi.setQuantity(ci.getQuantity());
            oi.setUnitPrice(ci.getUnitPrice());

            orderItemRepo.save(oi);
        }

        // close cart + clear cart items
        cart.setStatus(CartStatus.CHECKED_OUT);
        cartRepo.save(cart);
        cartItemRepo.deleteByCartId(cart.getId());

        return order.getId();
    }
}