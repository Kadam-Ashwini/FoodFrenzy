package com.app.food.model;


import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name="delivery_assignments",
       uniqueConstraints = @UniqueConstraint(name="uk_assignment_order", columnNames = "order_id"))
public class DeliveryAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="order_id", nullable=false)
    private FoodOrder order;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="agent_id", nullable=false)
    private DeliveryAgent agent;

    @Column(nullable=false)
    private LocalDateTime assignedAt = LocalDateTime.now();

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public FoodOrder getOrder() { return order; }
    public void setOrder(FoodOrder order) { this.order = order; }

    public DeliveryAgent getAgent() { return agent; }
    public void setAgent(DeliveryAgent agent) { this.agent = agent; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
}