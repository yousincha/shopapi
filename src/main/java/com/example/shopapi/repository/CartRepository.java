package com.example.shopapi.repository;

import com.example.shopapi.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository  extends JpaRepository<Cart, Long> {
    Optional<Cart> findByMemberIdAndDate(Long memberId, String date);

    List<Cart> findByMemberId(Long memberId);
}
