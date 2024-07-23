package com.example.shopapi.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class AdminSignupResponseDto {
    private String email;
    private LocalDateTime regdate;
}
