package com.example.shopapi.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class AdminSignupResponseDto {
    private Long adminId; // 또는 adminId
    private String email;
    private LocalDateTime regdate;
    private String role;
}
