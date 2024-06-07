package com.example.shopapi.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;


@Embeddable
@Setter
@Getter
public class Rating {
    private Double rate;
    private Integer count;
}

