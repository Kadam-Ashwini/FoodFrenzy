/*
 * package com.app.food.repository;
 * 
 * 
 * 
 * import java.util.Optional; import
 * org.springframework.data.jpa.repository.JpaRepository; import
 * com.app.food.model.DeliveryAssignment;
 * 
 * public interface DeliveryAssignmentRepository extends
 * JpaRepository<DeliveryAssignment, Long> { Optional<DeliveryAssignment>
 * findByOrderId(Long orderId); }
 */

package com.app.food.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.food.model.DeliveryAssignment;
import com.app.food.model.OrderStatus;

public interface DeliveryAssignmentRepository extends JpaRepository<DeliveryAssignment, Long> {

    Optional<DeliveryAssignment> findByOrderId(Long orderId);

    @Query("""
       select da
       from DeliveryAssignment da
       join fetch da.order o
       join fetch da.agent a
       where o.status = :status
    """)
    List<DeliveryAssignment> findAssignmentsByOrderStatus(@Param("status") OrderStatus status);
}