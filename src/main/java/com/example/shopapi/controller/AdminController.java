package com.example.shopapi.controller;

import com.example.shopapi.domain.Admin;
import com.example.shopapi.domain.Member;
import com.example.shopapi.domain.Role;
import com.example.shopapi.domain.RefreshToken;
import com.example.shopapi.dto.*;
import com.example.shopapi.security.jwt.util.JwtTokenizer;
import com.example.shopapi.service.AdminService;
import com.example.shopapi.service.RefreshTokenService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
@Validated
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final AdminService adminService;
    private final JwtTokenizer jwtTokenizer;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid AdminSignupDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Admin admin = new Admin();
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));

        Admin saveAdmin = adminService.addAdmin(admin);

        AdminSignupResponseDto adminSignupResponse = new AdminSignupResponseDto();
        adminSignupResponse.setAdminId(saveAdmin.getAdminId());
        adminSignupResponse.setEmail(saveAdmin.getEmail());

        return new ResponseEntity(adminSignupResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AdminLoginDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Admin admin = adminService.findByEmail(request.getEmail());
        if (admin == null || !passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이메일이나 암호가 틀렸습니다.");
        }

        List<String> roles = admin.getRoles().stream().map(Role::getName).collect(Collectors.toList());

        String accessToken = jwtTokenizer.createAdminAccessToken(admin.getAdminId(), admin.getEmail(), roles);
        String refreshToken = jwtTokenizer.createAdminRefreshToken(admin.getAdminId(), admin.getEmail(), roles);

        // Save the refresh token
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setValue(refreshToken);
        refreshTokenEntity.setAdminId(admin.getAdminId());
        refreshTokenService.addRefreshToken(refreshTokenEntity);

        AdminLoginResponseDto loginResponse = AdminLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .adminId (admin.getAdminId()) // 또는 adminId
                .build();
        return ResponseEntity.ok(loginResponse);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenDto refreshTokenDto) {
        refreshTokenService.deleteRefreshToken(refreshTokenDto.getRefreshToken());
        return ResponseEntity.ok("로그아웃되었습니다.");
    }
    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        String tokenValue = refreshTokenDto.getRefreshToken();
        RefreshToken refreshToken = refreshTokenService.findRefreshToken(tokenValue)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));

        Claims claims = jwtTokenizer.parseRefreshToken(refreshToken.getValue());

        Long adminId = jwtTokenizer.getAdminIdFromToken(tokenValue);
        Admin admin = adminService.getAdmin(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        List<String> roles = claims.get("roles", List.class); // Extract roles from claims
        String email = claims.getSubject();

        String accessToken = jwtTokenizer.createAdminAccessToken(adminId, email, roles);

        AdminLoginResponseDto loginResponse = AdminLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(tokenValue)
                .adminId(admin.getAdminId())
                .build();
        return ResponseEntity.ok(loginResponse);
    }


}
