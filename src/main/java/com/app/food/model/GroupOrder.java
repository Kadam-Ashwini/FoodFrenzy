package com.app.food.model;



import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name="group_orders",
       uniqueConstraints = @UniqueConstraint(name="uk_group_order_code", columnNames = "code"))
public class GroupOrder {

	
	@Column(name="final_order_id")
	private Long finalOrderId;

	private LocalDateTime finalizedAt;
	
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=12)
    private String code; // join code

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="host_user_id", nullable=false)
    private AppUser host;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="restaurant_id", nullable=false)
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private GroupOrderStatus status = GroupOrderStatus.OPEN;

    @Column(nullable=false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable=false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public AppUser getHost() { return host; }
    public void setHost(AppUser host) { this.host = host; }

    public Restaurant getRestaurant() { return restaurant; }
    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }

    public GroupOrderStatus getStatus() { return status; }
    public void setStatus(GroupOrderStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    
    
    
    public Long getFinalOrderId() { return finalOrderId; }
    public void setFinalOrderId(Long finalOrderId) { this.finalOrderId = finalOrderId; }

    public LocalDateTime getFinalizedAt() { return finalizedAt; }
    public void setFinalizedAt(LocalDateTime finalizedAt) { this.finalizedAt = finalizedAt; }
}