/*
 * package com.app.food.repository; import
 * org.springframework.data.jpa.repository.Query; import
 * org.springframework.data.repository.query.Param;
 * 
 * import com.app.food.model.PaymentStatus; import java.time.LocalDateTime;
 * import java.util.List;
 * 
 * import org.springframework.data.jpa.repository.JpaRepository; import
 * org.springframework.data.jpa.repository.Query; import
 * org.springframework.data.repository.query.Param;
 * 
 * import com.app.food.model.OrderItem; import com.app.food.model.PaymentStatus;
 * import com.app.food.repository.projection.TopFoodItemRow; import
 * java.util.List;
 * 
 * import org.springframework.data.domain.Pageable; import
 * org.springframework.data.jpa.repository.Query; import
 * org.springframework.data.repository.query.Param;
 * 
 * import com.app.food.model.OrderStatus; import
 * com.app.food.model.PaymentStatus; public interface OrderItemRepository
 * extends JpaRepository<OrderItem, Long> {
 * 
 * List<OrderItem> findByOrderId(Long orderId);
 * 
 * @Query(""" select oi.foodName as foodName, oi.category as category,
 * sum(oi.quantity) as totalQty, sum(oi.unitPrice * oi.quantity) as revenue from
 * OrderItem oi join oi.order o where o.paymentStatus = :paidStatus and
 * o.createdAt between :from and :to group by oi.foodName, oi.category order by
 * sum(oi.quantity) desc """) List<TopFoodItemRow>
 * topItemsPaidBetween(@Param("paidStatus") PaymentStatus paidStatus,
 * 
 * @Param("from") LocalDateTime from,
 * 
 * @Param("to") LocalDateTime to);
 * 
 * 
 * 
 * 
 * 
 * @Query(""" select oi.category as cat, sum(oi.quantity) as qty from OrderItem
 * oi join oi.order o where o.user.id = :userId and o.paymentStatus =
 * :paidStatus group by oi.category order by sum(oi.quantity) desc """)
 * List<Object[]> topCategoriesForUser(@Param("userId") Long userId,
 * 
 * @Param("paidStatus") PaymentStatus paidStatus);
 * 
 * @Query(""" select oi.foodName as name, sum(oi.quantity) as qty from OrderItem
 * oi join oi.order o where o.paymentStatus = :paidStatus group by oi.foodName
 * order by sum(oi.quantity) desc """) List<Object[]>
 * trendingFoodNames(@Param("paidStatus") PaymentStatus paidStatus);
 * 
 * 
 * 
 * 
 * 
 * 
 * @Query(""" select oi.category as cat, sum(oi.quantity) as qty from OrderItem
 * oi join oi.order o where o.user.id = :userId and o.status <> :cancelledStatus
 * and o.paymentStatus <> :failedStatus group by oi.category order by
 * sum(oi.quantity) desc """) List<Object[]>
 * topCategoriesForUserSmart(@Param("userId") Long userId,
 * 
 * @Param("cancelledStatus") OrderStatus cancelledStatus,
 * 
 * @Param("failedStatus") PaymentStatus failedStatus);
 * 
 * @Query(""" select oi.veg as veg, sum(oi.quantity) as qty from OrderItem oi
 * join oi.order o where o.user.id = :userId and o.status <> :cancelledStatus
 * and o.paymentStatus <> :failedStatus group by oi.veg """) List<Object[]>
 * vegPreferenceForUserSmart(@Param("userId") Long userId,
 * 
 * @Param("cancelledStatus") OrderStatus cancelledStatus,
 * 
 * @Param("failedStatus") PaymentStatus failedStatus);
 * 
 * @Query(""" select oi.foodName from OrderItem oi join oi.order o where
 * o.user.id = :userId and o.status <> :cancelledStatus order by o.createdAt
 * desc """) List<String> recentFoodNamesForUser(@Param("userId") Long userId,
 * 
 * @Param("cancelledStatus") OrderStatus cancelledStatus, Pageable pageable);
 * 
 * @Query(""" select oi.category as cat, sum(oi.quantity) as qty from OrderItem
 * oi join oi.order o where o.paymentStatus = :paidStatus and o.status <>
 * :cancelledStatus group by oi.category order by sum(oi.quantity) desc """)
 * List<Object[]> topCategoriesGlobal(@Param("paidStatus") PaymentStatus
 * paidStatus,
 * 
 * @Param("cancelledStatus") OrderStatus cancelledStatus);
 * 
 * List<OrderItem> findByOrderIdIn(List<Long> orderIds);
 * 
 * 
 * 
 * 
 * 
 * 
 * }
 */







// FILE: src/main/java/com/app/food/repository/OrderItemRepository.java
package com.app.food.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.food.model.OrderItem;
import com.app.food.model.OrderStatus;
import com.app.food.model.PaymentStatus;
import com.app.food.repository.projection.TopFoodItemRow;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);

    // ✅ For My Orders page: fetch items for many orders (and also fetch "order" to avoid lazy issues)
    @Query("""
       select oi
       from OrderItem oi
       join fetch oi.order o
       where o.id in :orderIds
    """)
    List<OrderItem> findByOrderIdsFetchOrder(@Param("orderIds") List<Long> orderIds);

    // ----- Reports -----
    @Query("""
        select
          oi.foodName as foodName,
          oi.category as category,
          sum(oi.quantity) as totalQty,
          sum(oi.unitPrice * oi.quantity) as revenue
        from OrderItem oi
        join oi.order o
        where o.paymentStatus = :paidStatus
          and o.createdAt between :from and :to
        group by oi.foodName, oi.category
        order by sum(oi.quantity) desc
    """)
    List<TopFoodItemRow> topItemsPaidBetween(@Param("paidStatus") PaymentStatus paidStatus,
                                            @Param("from") LocalDateTime from,
                                            @Param("to") LocalDateTime to);

    // ----- Recommendation queries -----
    @Query("""
       select oi.category as cat, sum(oi.quantity) as qty
       from OrderItem oi
       join oi.order o
       where o.user.id = :userId
         and o.paymentStatus = :paidStatus
       group by oi.category
       order by sum(oi.quantity) desc
    """)
    List<Object[]> topCategoriesForUser(@Param("userId") Long userId,
                                       @Param("paidStatus") PaymentStatus paidStatus);

    @Query("""
       select oi.foodName as name, sum(oi.quantity) as qty
       from OrderItem oi
       join oi.order o
       where o.paymentStatus = :paidStatus
       group by oi.foodName
       order by sum(oi.quantity) desc
    """)
    List<Object[]> trendingFoodNames(@Param("paidStatus") PaymentStatus paidStatus);

    @Query("""
       select oi.category as cat, sum(oi.quantity) as qty
       from OrderItem oi
       join oi.order o
       where o.user.id = :userId
         and o.status <> :cancelledStatus
         and o.paymentStatus <> :failedStatus
       group by oi.category
       order by sum(oi.quantity) desc
    """)
    List<Object[]> topCategoriesForUserSmart(@Param("userId") Long userId,
                                            @Param("cancelledStatus") OrderStatus cancelledStatus,
                                            @Param("failedStatus") PaymentStatus failedStatus);

    @Query("""
       select oi.veg as veg, sum(oi.quantity) as qty
       from OrderItem oi
       join oi.order o
       where o.user.id = :userId
         and o.status <> :cancelledStatus
         and o.paymentStatus <> :failedStatus
       group by oi.veg
    """)
    List<Object[]> vegPreferenceForUserSmart(@Param("userId") Long userId,
                                            @Param("cancelledStatus") OrderStatus cancelledStatus,
                                            @Param("failedStatus") PaymentStatus failedStatus);

    @Query("""
       select oi.foodName
       from OrderItem oi
       join oi.order o
       where o.user.id = :userId
         and o.status <> :cancelledStatus
       order by o.createdAt desc
    """)
    List<String> recentFoodNamesForUser(@Param("userId") Long userId,
                                       @Param("cancelledStatus") OrderStatus cancelledStatus,
                                       Pageable pageable);

    @Query("""
       select oi.category as cat, sum(oi.quantity) as qty
       from OrderItem oi
       join oi.order o
       where o.paymentStatus = :paidStatus
         and o.status <> :cancelledStatus
       group by oi.category
       order by sum(oi.quantity) desc
    """)
    List<Object[]> topCategoriesGlobal(@Param("paidStatus") PaymentStatus paidStatus,
                                      @Param("cancelledStatus") OrderStatus cancelledStatus);
}