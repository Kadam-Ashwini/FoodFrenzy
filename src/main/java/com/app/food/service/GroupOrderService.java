/*
 * package com.app.food.service;
 * 
 * 
 * 
 * import java.security.SecureRandom; import java.util.List;
 * 
 * import org.springframework.stereotype.Service; import
 * org.springframework.transaction.annotation.Transactional;
 * 
 * import com.app.food.model.*; import com.app.food.repository.*;
 * 
 * @Service public class GroupOrderService {
 * 
 * private final GroupOrderRepository groupRepo; private final
 * GroupOrderMemberRepository memberRepo; private final GroupOrderItemRepository
 * itemRepo;
 * 
 * private final AppUserRepository userRepo; private final RestaurantRepository
 * restaurantRepo; private final FoodItemRepository foodRepo;
 * 
 * private final SecureRandom random = new SecureRandom(); private static final
 * String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // no confusing chars
 * 
 * public GroupOrderService(GroupOrderRepository groupRepo,
 * GroupOrderMemberRepository memberRepo, GroupOrderItemRepository itemRepo,
 * AppUserRepository userRepo, RestaurantRepository restaurantRepo,
 * FoodItemRepository foodRepo) { this.groupRepo = groupRepo; this.memberRepo =
 * memberRepo; this.itemRepo = itemRepo; this.userRepo = userRepo;
 * this.restaurantRepo = restaurantRepo; this.foodRepo = foodRepo; }
 * 
 * // ----- Create -----
 * 
 * @Transactional public GroupOrder createGroup(Long hostUserId, Long
 * restaurantId) { AppUser host = userRepo.findById(hostUserId).orElseThrow();
 * Restaurant restaurant = restaurantRepo.findById(restaurantId).orElseThrow();
 * 
 * GroupOrder g = new GroupOrder(); g.setHost(host);
 * g.setRestaurant(restaurant); g.setStatus(GroupOrderStatus.OPEN);
 * g.setCode(generateUniqueCode(6)); // 6 characters
 * 
 * g = groupRepo.save(g);
 * 
 * // host automatically becomes a member GroupOrderMember m = new
 * GroupOrderMember(); m.setGroupOrder(g); m.setUser(host); memberRepo.save(m);
 * 
 * return g; }
 * 
 * // ----- Join -----
 * 
 * @Transactional public GroupOrder joinGroup(Long userId, String code) {
 * AppUser user = userRepo.findById(userId).orElseThrow();
 * 
 * GroupOrder g = groupRepo.findByCode(code.trim().toUpperCase())
 * .orElseThrow(() -> new RuntimeException("Invalid group code"));
 * 
 * if (g.getStatus() != GroupOrderStatus.OPEN) { throw new
 * RuntimeException("Group order is not open"); }
 * 
 * memberRepo.findByGroupOrderIdAndUserId(g.getId(), userId) .orElseGet(() -> {
 * GroupOrderMember m = new GroupOrderMember(); m.setGroupOrder(g);
 * m.setUser(user); return memberRepo.save(m); });
 * 
 * return g; }
 * 
 * // ----- Add item (member adds) -----
 * 
 * @Transactional public void addItem(Long userId, String code, Long foodItemId)
 * {
 * 
 * GroupOrder g = groupRepo.findByCode(code.trim().toUpperCase())
 * .orElseThrow(() -> new RuntimeException("Invalid group code"));
 * 
 * if (g.getStatus() != GroupOrderStatus.OPEN) { throw new
 * RuntimeException("Group order is not open"); }
 * 
 * // Must be member memberRepo.findByGroupOrderIdAndUserId(g.getId(), userId)
 * .orElseThrow(() -> new
 * RuntimeException("You are not a member of this group"));
 * 
 * FoodItem food = foodRepo.findById(foodItemId).orElseThrow();
 * 
 * // Ensure food belongs to same restaurant if
 * (!food.getRestaurant().getId().equals(g.getRestaurant().getId())) { throw new
 * RuntimeException("This item is from another restaurant"); }
 * 
 * GroupOrderItem existing = itemRepo
 * .findByGroupOrderIdAndFoodItemIdAndAddedById(g.getId(), foodItemId, userId)
 * .orElse(null);
 * 
 * if (existing == null) { GroupOrderItem gi = new GroupOrderItem();
 * gi.setGroupOrder(g); gi.setFoodItem(food);
 * gi.setAddedBy(userRepo.findById(userId).orElseThrow()); gi.setQuantity(1);
 * gi.setUnitPrice(food.getPrice()); itemRepo.save(gi); } else {
 * existing.setQuantity(existing.getQuantity() + 1); itemRepo.save(existing); }
 * }
 * 
 * // ----- Read ----- public GroupOrder getByCode(String code) { return
 * groupRepo.findByCode(code.trim().toUpperCase()) .orElseThrow(() -> new
 * RuntimeException("Invalid group code")); }
 * 
 * public List<GroupOrderMember> members(Long groupOrderId) { return
 * memberRepo.findByGroupOrderId(groupOrderId); }
 * 
 * public List<GroupOrderItem> items(Long groupOrderId) { return
 * itemRepo.findByGroupOrderId(groupOrderId); }
 * 
 * // ----- Helper: join code ----- private String generateUniqueCode(int len) {
 * String code; do { StringBuilder sb = new StringBuilder(len); for (int i = 0;
 * i < len; i++) { sb.append(CHARS.charAt(random.nextInt(CHARS.length()))); }
 * code = sb.toString(); } while (groupRepo.existsByCode(code)); return code; }
 * }
 */




package com.app.food.service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.food.model.*;
import com.app.food.repository.*;

@Service
public class GroupOrderService {

    private final GroupOrderRepository groupRepo;
    private final GroupOrderMemberRepository memberRepo;
    private final GroupOrderItemRepository itemRepo;

    private final AppUserRepository userRepo;
    private final RestaurantRepository restaurantRepo;
    private final FoodItemRepository foodRepo;

    private final FoodOrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;

    private final SecureRandom random = new SecureRandom();
    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    public GroupOrderService(GroupOrderRepository groupRepo,
                             GroupOrderMemberRepository memberRepo,
                             GroupOrderItemRepository itemRepo,
                             AppUserRepository userRepo,
                             RestaurantRepository restaurantRepo,
                             FoodItemRepository foodRepo,
                             FoodOrderRepository orderRepo,
                             OrderItemRepository orderItemRepo) {
        this.groupRepo = groupRepo;
        this.memberRepo = memberRepo;
        this.itemRepo = itemRepo;
        this.userRepo = userRepo;
        this.restaurantRepo = restaurantRepo;
        this.foodRepo = foodRepo;
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
    }

    // ---------- Create ----------
    @Transactional
    public GroupOrder createGroup(Long hostUserId, Long restaurantId) {
        AppUser host = userRepo.findById(hostUserId).orElseThrow();
        Restaurant restaurant = restaurantRepo.findById(restaurantId).orElseThrow();

        GroupOrder g = new GroupOrder();
        g.setHost(host);
        g.setRestaurant(restaurant);
        g.setStatus(GroupOrderStatus.OPEN);
        g.setCode(generateUniqueCode(6));

        g = groupRepo.save(g);

        // host auto-member
        GroupOrderMember m = new GroupOrderMember();
        m.setGroupOrder(g);
        m.setUser(host);
        memberRepo.save(m);

        return g;
    }

    // ---------- Join ----------
    @Transactional
    public GroupOrder joinGroup(Long userId, String code) {
        AppUser user = userRepo.findById(userId).orElseThrow();

        GroupOrder g = groupRepo.findByCode(code.trim().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Invalid group code"));

        if (g.getStatus() != GroupOrderStatus.OPEN) {
            throw new RuntimeException("Group order is not open");
        }

        memberRepo.findByGroupOrderIdAndUserId(g.getId(), userId)
                .orElseGet(() -> {
                    GroupOrderMember m = new GroupOrderMember();
                    m.setGroupOrder(g);
                    m.setUser(user);
                    return memberRepo.save(m);
                });

        return g;
    }

    // ---------- Add item ----------
    @Transactional
    public void addItem(Long userId, String code, Long foodItemId) {
        GroupOrder g = groupRepo.findByCode(code.trim().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Invalid group code"));

        if (g.getStatus() != GroupOrderStatus.OPEN) {
            throw new RuntimeException("Group order is not open");
        }

        // Must be member
        memberRepo.findByGroupOrderIdAndUserId(g.getId(), userId)
                .orElseThrow(() -> new RuntimeException("You are not a member of this group"));

        FoodItem food = foodRepo.findById(foodItemId).orElseThrow();

        // Ensure same restaurant
        if (!food.getRestaurant().getId().equals(g.getRestaurant().getId())) {
            throw new RuntimeException("This item is from another restaurant");
        }

        GroupOrderItem existing = itemRepo
                .findByGroupOrderIdAndFoodItemIdAndAddedById(g.getId(), foodItemId, userId)
                .orElse(null);

        if (existing == null) {
            GroupOrderItem gi = new GroupOrderItem();
            gi.setGroupOrder(g);
            gi.setFoodItem(food);
            gi.setAddedBy(userRepo.findById(userId).orElseThrow());
            gi.setQuantity(1);
            gi.setUnitPrice(food.getPrice());
            itemRepo.save(gi);
        } else {
            existing.setQuantity(existing.getQuantity() + 1);
            itemRepo.save(existing);
        }
    }

    // ---------- Finalize ----------
    @Transactional
    public Long finalizeGroup(Long hostUserId, String code, String deliveryAddress, String city) {

        GroupOrder g = groupRepo.findByCode(code.trim().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Invalid group code"));

        if (g.getStatus() != GroupOrderStatus.OPEN) {
            throw new RuntimeException("Group is not open");
        }

        if (!g.getHost().getId().equals(hostUserId)) {
            throw new RuntimeException("Only host can finalize");
        }

        List<GroupOrderItem> groupItems = itemRepo.findByGroupOrderId(g.getId());
        if (groupItems.isEmpty()) {
            throw new RuntimeException("No items in group order");
        }

        // total
        BigDecimal total = groupItems.stream()
                .map(GroupOrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // create normal order (host is payer)
        AppUser host = userRepo.findById(hostUserId).orElseThrow();

        FoodOrder order = new FoodOrder();
        order.setUser(host);
        order.setRestaurant(g.getRestaurant());
        order.setStatus(OrderStatus.CREATED);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setTotalAmount(total);
        order.setDeliveryAddress(deliveryAddress);
        order.setCity(city);

        order = orderRepo.save(order);

        // create order items (snapshot)
        for (GroupOrderItem gi : groupItems) {
            FoodItem f = gi.getFoodItem();

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setFoodName(f.getName());
            oi.setCategory(f.getCategory());
            oi.setVeg(f.isVeg());
            oi.setQuantity(gi.getQuantity());
            oi.setUnitPrice(gi.getUnitPrice());

            orderItemRepo.save(oi);
        }

        // lock the group
        g.setStatus(GroupOrderStatus.FINALIZED);
        g.setFinalOrderId(order.getId());
        g.setFinalizedAt(LocalDateTime.now());
        groupRepo.save(g);

        return order.getId();
    }

    // ---------- Read ----------
    public GroupOrder getByCode(String code) {
        return groupRepo.findByCode(code.trim().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Invalid group code"));
    }

    public List<GroupOrderMember> members(Long groupOrderId) {
        return memberRepo.findByGroupOrderId(groupOrderId);
    }

    public List<GroupOrderItem> items(Long groupOrderId) {
        return itemRepo.findByGroupOrderId(groupOrderId);
    }

    public BigDecimal groupTotal(Long groupOrderId) {
        return itemRepo.findByGroupOrderId(groupOrderId).stream()
                .map(GroupOrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ---------- Helper ----------
    private String generateUniqueCode(int len) {
        String code;
        do {
            StringBuilder sb = new StringBuilder(len);
            for (int i = 0; i < len; i++) {
                sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
            }
            code = sb.toString();
        } while (groupRepo.existsByCode(code));
        return code;
    }
}