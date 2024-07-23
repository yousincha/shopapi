package com.example.shopapi.repository;

import com.example.shopapi.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    // 이메일로 Admin 조회
    Admin findByEmail(String email);
}
