package com.app.food.model;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "payment_transactions")
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="order_id", nullable=false)
    private FoodOrder order;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private PaymentStatus status;

    @Column(nullable=false, precision=10, scale=2)
    private BigDecimal amount;

    @Column(length=80)
    private String ref; // mock reference like PAY-XXXX

    @Column(length=200)
    private String message; // failure reason etc.

    @Column(nullable=false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public FoodOrder getOrder() { return order; }
    public void setOrder(FoodOrder order) { this.order = order; }

    public PaymentMethod getMethod() { return method; }
    public void setMethod(PaymentMethod method) { this.method = method; }

    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getRef() { return ref; }
    public void setRef(String ref) { this.ref = ref; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}