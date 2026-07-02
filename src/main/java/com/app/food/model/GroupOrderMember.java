package com.app.food.model;


import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name="group_order_members",
       uniqueConstraints = @UniqueConstraint(
           name="uk_group_member",
           columnNames = {"group_order_id", "user_id"}
       ))
public class GroupOrderMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="group_order_id", nullable=false)
    private GroupOrder groupOrder;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private AppUser user;

    @Column(nullable=false)
    private LocalDateTime joinedAt = LocalDateTime.now();

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public GroupOrder getGroupOrder() { return groupOrder; }
    public void setGroupOrder(GroupOrder groupOrder) { this.groupOrder = groupOrder; }

    public AppUser getUser() { return user; }
    public void setUser(AppUser user) { this.user = user; }

    public LocalDateTime getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }
}