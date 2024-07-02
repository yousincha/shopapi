package com.example.shopapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product2 {
    // imageUrl, 상품명, 가격, 상품id
    private String imageUrl;
    private String name;
    private int price;
    private Long id;
}
