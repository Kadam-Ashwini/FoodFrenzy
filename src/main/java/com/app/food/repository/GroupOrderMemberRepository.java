package com.app.food.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.food.model.GroupOrderMember;

public interface GroupOrderMemberRepository extends JpaRepository<GroupOrderMember, Long> {
    List<GroupOrderMember> findByGroupOrderId(Long groupOrderId);
    Optional<GroupOrderMember> findByGroupOrderIdAndUserId(Long groupOrderId, Long userId);
}