package com.example.shopapi.dto;

import lombok.Data;

import jakarta.validation.constraints.*;

@Data
public class RefreshTokenDto {
    @NotEmpty
    String refreshToken;
}
