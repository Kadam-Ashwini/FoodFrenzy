package com.app.food.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.app.food.model.FoodItem;
import com.app.food.model.OrderStatus;
import com.app.food.model.PaymentStatus;
import com.app.food.repository.FoodItemRepository;
import com.app.food.repository.OrderItemRepository;

@Service
public class RecommendationService {

    private final OrderItemRepository orderItemRepo;
    private final FoodItemRepository foodRepo;

    public RecommendationService(OrderItemRepository orderItemRepo, FoodItemRepository foodRepo) {
        this.orderItemRepo = orderItemRepo;
        this.foodRepo = foodRepo;
    }

    /** Home page recommendations (global across restaurants) */
    public RecommendationResult recommendSectionForUser(Long userId) {
        RecommendationContext ctx = buildContext(userId);

        Set<FoodItem> result = new LinkedHashSet<>();

        String reason = null;

        // user categories first
        if (!ctx.topCategories.isEmpty()) {
            String cat = ctx.topCategories.get(0);
            reason = "Because you ordered: " + cat;

            for (String c : ctx.topCategories) {
                List<FoodItem> candidates =
                        foodRepo.findTop30ByCategoryIgnoreCaseAndAvailableTrueAndRestaurantActiveTrueOrderByIdDesc(c);

                addCandidates(result, candidates, ctx.recentFoodNames, ctx.preferVeg, 12);
                if (result.size() >= 12) break;
            }
        }

        // fallback: trending/new items
        if (result.isEmpty()) {
            reason = "Trending now";
            result.addAll(foodRepo.findTop30ByAvailableTrueAndRestaurantActiveTrueOrderByIdDesc());
        }

        List<FoodItem> items = new ArrayList<>(result);
        if (items.size() > 12) items = items.subList(0, 12);

        // If we are showing veg-preferred list, mention it
        if (ctx.preferVeg && reason != null && !reason.contains("(Veg picks)")) {
            reason = reason + " (Veg picks)";
        }

        return new RecommendationResult(reason, items);
    }

    /** Restaurant menu recommendations (from the same restaurant) */
    public RecommendationResult recommendForUserInRestaurant(Long userId, Long restaurantId) {
        RecommendationContext ctx = buildContext(userId);

        Set<FoodItem> result = new LinkedHashSet<>();
        String reason = null;

        if (!ctx.topCategories.isEmpty()) {
            String cat = ctx.topCategories.get(0);
            reason = "Recommended because you ordered: " + cat;

            // try same restaurant + that category
            List<FoodItem> candidates =
                    foodRepo.findTop12ByRestaurantIdAndCategoryIgnoreCaseAndAvailableTrueOrderByIdDesc(restaurantId, cat);

            addCandidates(result, candidates, ctx.recentFoodNames, ctx.preferVeg, 6);
        }

        // fallback: show popular/latest items in this restaurant
        if (result.isEmpty()) {
            reason = "Popular in this restaurant";
            List<FoodItem> candidates = foodRepo.findTop12ByRestaurantIdAndAvailableTrueOrderByIdDesc(restaurantId);
            addCandidates(result, candidates, ctx.recentFoodNames, ctx.preferVeg, 6);
        }

        List<FoodItem> items = new ArrayList<>(result);
        if (items.size() > 6) items = items.subList(0, 6);

        if (ctx.preferVeg && reason != null && !reason.contains("(Veg picks)")) {
            reason = reason + " (Veg picks)";
        }

        return new RecommendationResult(reason, items);
    }

    // ----------------- helpers -----------------

    private void addCandidates(Set<FoodItem> result,
                               List<FoodItem> candidates,
                               Set<String> recentFoodNames,
                               boolean preferVeg,
                               int limit) {

        // add veg first if preferVeg
        if (preferVeg) {
            for (FoodItem f : candidates) {
                if (result.size() >= limit) return;
                if (recentFoodNames.contains(f.getName())) continue;
                if (!f.isVeg()) continue;
                result.add(f);
            }
        }

        // add remaining
        for (FoodItem f : candidates) {
            if (result.size() >= limit) return;
            if (recentFoodNames.contains(f.getName())) continue;
            result.add(f);
        }
    }

    private RecommendationContext buildContext(Long userId) {

        // avoid recommending items user ordered recently
        List<String> recentNames = orderItemRepo.recentFoodNamesForUser(
                userId, OrderStatus.CANCELLED, PageRequest.of(0, 30));
        Set<String> recentSet = new LinkedHashSet<>(recentNames);

        // veg preference
        boolean preferVeg = false;
        long vegQty = 0;
        long nonVegQty = 0;

        var prefRows = orderItemRepo.vegPreferenceForUserSmart(
                userId, OrderStatus.CANCELLED, PaymentStatus.FAILED);

        for (Object[] r : prefRows) {
            Boolean veg = (Boolean) r[0];
            Long qty = (Long) r[1];
            long q = (qty == null ? 0 : qty);

            if (veg != null && veg) vegQty += q;
            else nonVegQty += q;
        }

        long totalPref = vegQty + nonVegQty;
        if (totalPref > 0) {
            double vegRatio = (double) vegQty / (double) totalPref;
            preferVeg = vegRatio >= 0.70;
        }

        // top categories for user
        List<String> categories = new ArrayList<>();
        var userCatRows = orderItemRepo.topCategoriesForUserSmart(
                userId, OrderStatus.CANCELLED, PaymentStatus.FAILED);

        for (Object[] r : userCatRows) {
            String cat = (String) r[0];
            if (cat != null && !cat.isBlank()) categories.add(cat);
            if (categories.size() >= 3) break;
        }

        // fallback to global categories if user has none
        if (categories.isEmpty()) {
            var globalRows = orderItemRepo.topCategoriesGlobal(PaymentStatus.PAID, OrderStatus.CANCELLED);
            for (Object[] r : globalRows) {
                String cat = (String) r[0];
                if (cat != null && !cat.isBlank()) categories.add(cat);
                if (categories.size() >= 3) break;
            }
        }

        return new RecommendationContext(preferVeg, recentSet, categories);
    }

    private static class RecommendationContext {
        final boolean preferVeg;
        final Set<String> recentFoodNames;
        final List<String> topCategories;

        RecommendationContext(boolean preferVeg, Set<String> recentFoodNames, List<String> topCategories) {
            this.preferVeg = preferVeg;
            this.recentFoodNames = recentFoodNames;
            this.topCategories = topCategories;
        }
    }
}