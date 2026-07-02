package com.app.food.model;



import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name="restaurant_feedback",
       uniqueConstraints = @UniqueConstraint(name="uk_feedback_order", columnNames = "order_id"))
public class RestaurantFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private AppUser user;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="restaurant_id", nullable=false)
    private Restaurant restaurant;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="order_id", nullable=false)
    private FoodOrder order;

    @Column(nullable=false)
    private int rating; // 1..5

    @Column(length=500)
    private String comment;

    @Column(nullable=false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public AppUser getUser() { return user; }
    public void setUser(AppUser user) { this.user = user; }

    public Restaurant getRestaurant() { return restaurant; }
    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }

    public FoodOrder getOrder() { return order; }
    public void setOrder(FoodOrder order) { this.order = order; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}