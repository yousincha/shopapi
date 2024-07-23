package com.example.shopapi.controller;

import com.example.shopapi.domain.Admin;
import com.example.shopapi.dto.AdminLoginDto;
import com.example.shopapi.dto.AdminLoginResponseDto;
import com.example.shopapi.dto.AdminSignupDto;
import com.example.shopapi.dto.AdminSignupResponseDto;
import com.example.shopapi.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminLoginDto request) {
        Admin admin = adminService.login(request.getEmail(), request.getPassword());
        if (admin != null) {
            // 로그인 성공 시 AdminLoginResponseDto 객체를 반환
            AdminLoginResponseDto responseDto = new AdminLoginResponseDto();
            responseDto.setEmail(admin.getEmail());
            responseDto.setRole("ADMIN");
            return ResponseEntity.ok(responseDto);
        } else {
            // 로그인 실패 시 401 Unauthorized 응답
            return ResponseEntity.status(401).body("이메일이나 암호가 틀렸습니다.");
        }
    }
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody AdminSignupDto request) {
        Admin newAdmin = adminService.signup(request.getEmail(), request.getPassword());
        AdminSignupResponseDto responseDto = new AdminSignupResponseDto();
        responseDto.setEmail(newAdmin.getEmail());

        return ResponseEntity.status(201).body(responseDto); // 201 Created
    }
}
