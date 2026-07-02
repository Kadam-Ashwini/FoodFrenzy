package com.app.food.tracking;

import org.springframework.web.bind.annotation.*;

import com.app.food.repository.DeliveryAssignmentRepository;
import com.app.food.repository.DeliveryLocationRepository;
import com.app.food.repository.FoodOrderRepository;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class TrackingApiController {

    private final FoodOrderRepository orderRepo;
    private final DeliveryAssignmentRepository assignmentRepo;
    private final DeliveryLocationRepository locationRepo;

    public TrackingApiController(FoodOrderRepository orderRepo,
                                 DeliveryAssignmentRepository assignmentRepo,
                                 DeliveryLocationRepository locationRepo) {
        this.orderRepo = orderRepo;
        this.assignmentRepo = assignmentRepo;
        this.locationRepo = locationRepo;
    }

    @GetMapping("/track/{orderId}")
    public TrackingDto track(@PathVariable Long orderId, HttpSession session) {

        Object uid = session.getAttribute("userId");
        if (uid == null) throw new RuntimeException("Not logged in");
        Long userId = (Long) uid;

        // only order owner can track
        var order = orderRepo.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        TrackingDto dto = new TrackingDto();
        dto.orderId = order.getId();
        dto.status = order.getStatus().name();

        var asg = assignmentRepo.findByOrderId(orderId).orElse(null);
        if (asg != null) {
            dto.agentName = asg.getAgent().getName();
            dto.agentPhone = asg.getAgent().getPhone();
            dto.vehicleNo = asg.getAgent().getVehicleNo();
        }

        var last = locationRepo.findTop1ByOrderIdOrderByIdDesc(orderId).orElse(null);
        if (last != null) {
            dto.lat = last.getLat();
            dto.lng = last.getLng();
            dto.lastUpdated = last.getCreatedAt();
        }

        return dto;
    }
}