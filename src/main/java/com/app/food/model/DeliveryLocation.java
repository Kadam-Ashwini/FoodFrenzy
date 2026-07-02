package com.app.food.model;



import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name="delivery_locations")
public class DeliveryLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="agent_id", nullable=false)
    private DeliveryAgent agent;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="order_id", nullable=false)
    private FoodOrder order;

    @Column(nullable=false)
    private double lat;

    @Column(nullable=false)
    private double lng;

    @Column(nullable=false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public DeliveryAgent getAgent() { return agent; }
    public void setAgent(DeliveryAgent agent) { this.agent = agent; }

    public FoodOrder getOrder() { return order; }
    public void setOrder(FoodOrder order) { this.order = order; }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLng() { return lng; }
    public void setLng(double lng) { this.lng = lng; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}