package com.example.shopapi.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // 모든 필드를 포함하는 생성자 추가
@Builder
public class AdminLoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private Long adminId;

}