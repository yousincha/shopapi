package com.example.shopapi.service;

import com.example.shopapi.domain.Admin;
import com.example.shopapi.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Admin login(String email, String password) {
        Admin admin = adminRepository.findByEmail(email);
        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
            return admin; // 로그인 성공
        }
        return null; // 로그인 실패
    }
    public Admin signup(String email, String password) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        // 새 Admin 객체 생성
        Admin admin = new Admin();
        admin.setEmail(email);
        admin.setPassword(encodedPassword);

        // Admin 객체를 데이터베이스에 저장
        return adminRepository.save(admin);
    }
}
