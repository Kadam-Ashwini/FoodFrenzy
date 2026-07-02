package com.app.food.service;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.food.model.DeliveryAgent;
import com.app.food.model.DeliveryAssignment;
import com.app.food.model.FoodOrder;
import com.app.food.model.OrderStatus;
import com.app.food.repository.DeliveryAgentRepository;
import com.app.food.repository.DeliveryAssignmentRepository;
import com.app.food.repository.FoodOrderRepository;

@Service
public class DeliveryService {

    private final DeliveryAgentRepository agentRepo;
    private final DeliveryAssignmentRepository assignmentRepo;
    private final FoodOrderRepository orderRepo;

    public DeliveryService(DeliveryAgentRepository agentRepo,
                           DeliveryAssignmentRepository assignmentRepo,
                           FoodOrderRepository orderRepo) {
        this.agentRepo = agentRepo;
        this.assignmentRepo = assignmentRepo;
        this.orderRepo = orderRepo;
    }

    public List<DeliveryAgent> activeAgents() {
        return agentRepo.findByActiveTrueOrderByIdDesc();
    }

    public DeliveryAssignment assignmentOrNull(Long orderId) {
        return assignmentRepo.findByOrderId(orderId).orElse(null);
    }

    @Transactional
    public void assignAgentAndStartDelivery(Long orderId, Long agentId) {
        FoodOrder order = orderRepo.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Cannot assign delivery for CANCELLED/DELIVERED order");
        }

        DeliveryAgent agent = agentRepo.findById(agentId).orElseThrow(() -> new RuntimeException("Agent not found"));
        if (!agent.isActive()) throw new RuntimeException("Agent is not active");

        DeliveryAssignment existing = assignmentRepo.findByOrderId(orderId).orElse(null);
        if (existing == null) {
            DeliveryAssignment da = new DeliveryAssignment();
            da.setOrder(order);
            da.setAgent(agent);
            assignmentRepo.save(da);
        } else {
            existing.setAgent(agent);
            assignmentRepo.save(existing);
        }

        // move order to OUT_FOR_DELIVERY
        order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
        orderRepo.save(order);
    }
}