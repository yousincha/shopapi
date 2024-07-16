// Address.java

package com.example.shopapi.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    private Long memberId; // 회원 ID
    @Setter
    @Getter
    private String recipientName; // 수령인 이름
    @Setter
    @Getter
    private String recipientPhone; // 수령인 전화번호
    @Setter
    @Getter
    private String postalCode; // 우편번호
    @Setter
    @Getter
    private String address; // 주소

    public Address() {
    }

    public Address(Long memberId, String recipientName, String recipientPhone, String postalCode, String address) {
        this.memberId = memberId;
        this.recipientName = recipientName;
        this.recipientPhone = recipientPhone;
        this.postalCode = postalCode;
        this.address = address;
    }

    // getter, setter
}
