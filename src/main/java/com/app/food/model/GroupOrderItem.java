package com.app.food.model;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name="group_order_items",
       uniqueConstraints = @UniqueConstraint(
           name="uk_group_item_member",
           columnNames = {"group_order_id","food_item_id","added_by_user_id"}
       ))
public class GroupOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="group_order_id", nullable=false)
    private GroupOrder groupOrder;

    @ManyToOne(optional=false, fetch = FetchType.EAGER)
    @JoinColumn(name="food_item_id", nullable=false)
    private FoodItem foodItem;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="added_by_user_id", nullable=false)
    private AppUser addedBy;

    @Column(nullable=false)
    private int quantity = 1;

    @Column(nullable=false, precision=10, scale=2)
    private BigDecimal unitPrice;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public GroupOrder getGroupOrder() { return groupOrder; }
    public void setGroupOrder(GroupOrder groupOrder) { this.groupOrder = groupOrder; }

    public FoodItem getFoodItem() { return foodItem; }
    public void setFoodItem(FoodItem foodItem) { this.foodItem = foodItem; }

    public AppUser getAddedBy() { return addedBy; }
    public void setAddedBy(AppUser addedBy) { this.addedBy = addedBy; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    @Transient
    public BigDecimal getLineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}