package com.app.food.model;



import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name="cart_items",
       uniqueConstraints = @UniqueConstraint(
           name="uk_cart_food",
           columnNames = {"cart_id","food_item_id"}
       ))
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id", nullable=false)
    private Cart cart;

    @ManyToOne(optional=false, fetch = FetchType.EAGER)
    @JoinColumn(name="food_item_id", nullable=false)
    private FoodItem foodItem;

    @Column(nullable=false)
    private int quantity = 1;

    @Column(nullable=false, precision=10, scale=2)
    private BigDecimal unitPrice; // price at time of adding

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }

    public FoodItem getFoodItem() { return foodItem; }
    public void setFoodItem(FoodItem foodItem) { this.foodItem = foodItem; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    @Transient
    public BigDecimal getLineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}