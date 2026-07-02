package com.app.food.service;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.food.model.*;
import com.app.food.repository.*;

@Service
public class CartService {

    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final AppUserRepository userRepo;
    private final RestaurantRepository restaurantRepo;
    private final FoodItemRepository foodRepo;

    public CartService(CartRepository cartRepo,
                       CartItemRepository cartItemRepo,
                       AppUserRepository userRepo,
                       RestaurantRepository restaurantRepo,
                       FoodItemRepository foodRepo) {
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
        this.userRepo = userRepo;
        this.restaurantRepo = restaurantRepo;
        this.foodRepo = foodRepo;
    }

    public Cart getOpenCartOrNull(Long userId) {
        return cartRepo.findByUserIdAndStatus(userId, CartStatus.OPEN).orElse(null);
    }

    public List<CartItem> getItems(Long cartId) {
        return cartItemRepo.findByCartId(cartId);
    }

    public BigDecimal calculateTotal(List<CartItem> items) {
        return items.stream()
                .map(CartItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public String addToCart(Long userId, Long foodItemId) {
        AppUser user = userRepo.findById(userId).orElseThrow();
        FoodItem food = foodRepo.findById(foodItemId).orElseThrow();
        Restaurant restaurant = food.getRestaurant();

        Cart cart = cartRepo.findByUserIdAndStatus(userId, CartStatus.OPEN).orElse(null);

        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart.setRestaurant(restaurant);
            cart.setStatus(CartStatus.OPEN);
            cart = cartRepo.save(cart);
        } else {
            // enforce single-restaurant cart
            if (!cart.getRestaurant().getId().equals(restaurant.getId())) {
                return "Cart has items from another restaurant. Please clear cart first.";
            }
        }

        CartItem item = cartItemRepo.findByCartIdAndFoodItemId(cart.getId(), foodItemId).orElse(null);
        if (item == null) {
            item = new CartItem();
            item.setCart(cart);
            item.setFoodItem(food);
            item.setQuantity(1);
            item.setUnitPrice(food.getPrice());
        } else {
            item.setQuantity(item.getQuantity() + 1);
        }

        cartItemRepo.save(item);
        return null; // null means success
    }

    @Transactional
    public void updateQty(Long userId, Long cartItemId, int qty) {
        if (qty < 1) qty = 1;

        Cart cart = cartRepo.findByUserIdAndStatus(userId, CartStatus.OPEN).orElseThrow();
        CartItem item = cartItemRepo.findById(cartItemId).orElseThrow();

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Invalid cart item");
        }

        item.setQuantity(qty);
        cartItemRepo.save(item);
    }

    @Transactional
    public void removeItem(Long userId, Long cartItemId) {
        Cart cart = cartRepo.findByUserIdAndStatus(userId, CartStatus.OPEN).orElseThrow();
        CartItem item = cartItemRepo.findById(cartItemId).orElseThrow();

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Invalid cart item");
        }
        cartItemRepo.delete(item);
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepo.findByUserIdAndStatus(userId, CartStatus.OPEN).orElse(null);
        if (cart == null) return;
        cartItemRepo.deleteByCartId(cart.getId());
    }
}