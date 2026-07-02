/*
 * package com.app.food.repository;
 * 
 * 
 * import java.util.Optional; import
 * org.springframework.data.jpa.repository.JpaRepository; import
 * com.app.food.model.DeliveryLocation;
 * 
 * public interface DeliveryLocationRepository extends
 * JpaRepository<DeliveryLocation, Long> { Optional<DeliveryLocation>
 * findTop1ByOrderIdOrderByIdDesc(Long orderId); }
 */


package com.app.food.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.food.model.DeliveryLocation;

public interface DeliveryLocationRepository extends JpaRepository<DeliveryLocation, Long> {

    Optional<DeliveryLocation> findTop1ByOrderIdOrderByIdDesc(Long orderId);

    long countByOrderId(Long orderId);
}