/*
 * package com.app.food.tracking;
 * 
 * 
 * import java.security.SecureRandom;
 * 
 * import org.springframework.scheduling.annotation.Scheduled; import
 * org.springframework.stereotype.Component; import
 * org.springframework.transaction.annotation.Transactional;
 * 
 * import com.app.food.model.DeliveryLocation; import
 * com.app.food.model.OrderStatus; import
 * com.app.food.repository.DeliveryAssignmentRepository; import
 * com.app.food.repository.DeliveryLocationRepository; import
 * com.app.food.repository.FoodOrderRepository;
 * 
 * @Component public class DeliveryLocationSimulator {
 * 
 * private final DeliveryAssignmentRepository assignmentRepo; private final
 * DeliveryLocationRepository locationRepo; private final FoodOrderRepository
 * orderRepo;
 * 
 * private final SecureRandom random = new SecureRandom();
 * 
 * public DeliveryLocationSimulator(DeliveryAssignmentRepository assignmentRepo,
 * DeliveryLocationRepository locationRepo, FoodOrderRepository orderRepo) {
 * this.assignmentRepo = assignmentRepo; this.locationRepo = locationRepo;
 * this.orderRepo = orderRepo; }
 * 
 * 
 * 
 * 
 * @Scheduled(fixedRate = 5000)
 * 
 * @Transactional public void tick() {
 * 
 * var assignments =
 * assignmentRepo.findAssignmentsByOrderStatus(OrderStatus.OUT_FOR_DELIVERY);
 * 
 * for (var asg : assignments) { Long orderId = asg.getOrder().getId();
 * 
 * // ✅ Stop adding infinite points (but DO NOT set DELIVERED automatically)
 * long points = locationRepo.countByOrderId(orderId); if (points >= 30) { // We
 * stop simulation after 30 points so DB won't grow. // Order will remain
 * OUT_FOR_DELIVERY until Admin/User marks DELIVERED. continue; }
 * 
 * var lastOpt = locationRepo.findTop1ByOrderIdOrderByIdDesc(orderId);
 * 
 * double lat; double lng;
 * 
 * if (lastOpt.isPresent()) { lat = lastOpt.get().getLat(); lng =
 * lastOpt.get().getLng(); } else { String city = asg.getOrder().getCity() ==
 * null ? "" : asg.getOrder().getCity().toLowerCase();
 * 
 * if (city.contains("chennai")) { lat = 13.0827; lng = 80.2707; } else if
 * (city.contains("hyderabad")) { lat = 17.3850; lng = 78.4867; } else if
 * (city.contains("mumbai")) { lat = 19.0760; lng = 72.8777; } else if
 * (city.contains("delhi")) { lat = 28.6139; lng = 77.2090; } else if
 * (city.contains("bangalore")) { lat = 12.9716; lng = 77.5946; } else { lat =
 * 17.3850; lng = 78.4867; }
 * 
 * lat += (random.nextDouble() - 0.5) * 0.02; lng += (random.nextDouble() - 0.5)
 * * 0.02; }
 * 
 * double dLat = (random.nextDouble() - 0.5) * 0.002; double dLng =
 * (random.nextDouble() - 0.5) * 0.002;
 * 
 * DeliveryLocation loc = new DeliveryLocation(); loc.setOrder(asg.getOrder());
 * loc.setAgent(asg.getAgent()); loc.setLat(lat + dLat); loc.setLng(lng + dLng);
 * 
 * locationRepo.save(loc); } } }
 */



package com.app.food.tracking;

import java.security.SecureRandom;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.app.food.model.DeliveryLocation;
import com.app.food.model.OrderStatus;
import com.app.food.repository.DeliveryAssignmentRepository;
import com.app.food.repository.DeliveryLocationRepository;
import com.app.food.repository.FoodOrderRepository;

@Component
public class DeliveryLocationSimulator {

    private final DeliveryAssignmentRepository assignmentRepo;
    private final DeliveryLocationRepository locationRepo;
    private final FoodOrderRepository orderRepo;

    private final SecureRandom random = new SecureRandom();

    public DeliveryLocationSimulator(DeliveryAssignmentRepository assignmentRepo,
                                     DeliveryLocationRepository locationRepo,
                                     FoodOrderRepository orderRepo) {
        this.assignmentRepo = assignmentRepo;
        this.locationRepo = locationRepo;
        this.orderRepo = orderRepo;
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void tick() {

        var assignments = assignmentRepo.findAssignmentsByOrderStatus(OrderStatus.OUT_FOR_DELIVERY);

        for (var asg : assignments) {
            Long orderId = asg.getOrder().getId();

            // stop adding infinite points (do NOT auto-deliver)
            long points = locationRepo.countByOrderId(orderId);
            if (points >= 30) {
                continue;
            }

            var lastOpt = locationRepo.findTop1ByOrderIdOrderByIdDesc(orderId);

            double lat;
            double lng;

            if (lastOpt.isPresent()) {
                lat = lastOpt.get().getLat();
                lng = lastOpt.get().getLng();
            } else {
                String city = asg.getOrder().getCity() == null ? "" : asg.getOrder().getCity().toLowerCase();

                if (city.contains("chennai")) { lat = 13.0827; lng = 80.2707; }
                else if (city.contains("hyderabad")) { lat = 17.3850; lng = 78.4867; }
                else if (city.contains("mumbai")) { lat = 19.0760; lng = 72.8777; }
                else if (city.contains("nashik")) { lat = 19.9975; lng = 73.7898; }
                else if (city.contains("delhi")) { lat = 28.6139; lng = 77.2090; }
                else if (city.contains("bangalore")) { lat = 12.9716; lng = 77.5946; }
                else { lat = 17.3850; lng = 78.4867; } // fallback

                // small random offset at start
                lat += (random.nextDouble() - 0.5) * 0.02;
                lng += (random.nextDouble() - 0.5) * 0.02;
            }

            // random walk movement
            double dLat = (random.nextDouble() - 0.5) * 0.002;
            double dLng = (random.nextDouble() - 0.5) * 0.002;

            DeliveryLocation loc = new DeliveryLocation();
            loc.setOrder(asg.getOrder());
            loc.setAgent(asg.getAgent());
            loc.setLat(lat + dLat);
            loc.setLng(lng + dLng);

            locationRepo.save(loc);
        }
    }
}