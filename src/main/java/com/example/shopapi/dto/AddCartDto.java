package com.example.shopapi.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCartDto {
    private Long memberId;
}
