package com.app.food.model;



import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name="order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="order_id", nullable=false)
    private FoodOrder order;

    // Snapshot fields (so order remains correct even if food item changes later)
    @Column(nullable=false, length=120)
    private String foodName;

    @Column(nullable=false, length=40)
    private String category;

    @Column(nullable=false)
    private boolean veg;

    @Column(nullable=false)
    private int quantity;

    @Column(nullable=false, precision=10, scale=2)
    private BigDecimal unitPrice;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public FoodOrder getOrder() { return order; }
    public void setOrder(FoodOrder order) { this.order = order; }

    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isVeg() { return veg; }
    public void setVeg(boolean veg) { this.veg = veg; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    @Transient
    public BigDecimal getLineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}