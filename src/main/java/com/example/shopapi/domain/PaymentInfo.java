package com.example.shopapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "paymentinfo")
@Getter
@Setter
public class PaymentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "imp_uid")
    private String impUid;

//    @Column(name = "amount")
//    private Long amount;

    @JsonProperty("paid_amount")
    @Column(name = "paid_amount")
    private Long paidAmount;

    @Column(name = "pay_method")
    private String payMethod;

    @Column(name = "merchant_uid")
    private String merchantUid;

    @Column(name = "product_name")
    private String name;

//    @Column(name = "buyer_email")
//    private String buyerEmail;

    @Column(name = "buyer_name")
    private String buyerName;

    @Column(name = "buyer_tel")
    private String buyerTel;

    @Column(name = "buyer_addr")
    private String buyerAddr;

    @Column(name = "buyer_postcode")
    private String buyerPostcode;

    @Override
    public String toString() {
        return "PaymentInfo{" +
                "id=" + id +
                ", impUid='" + impUid + '\'' +
//                ", amount=" + amount +
                ", paidAmount=" + paidAmount +
                ", payMethod='" + payMethod + '\'' +
                ", merchantUid='" + merchantUid + '\'' +
                ", product_name='" + name + '\'' +
//                ", buyerEmail='" + buyerEmail + '\'' +
                ", buyerName='" + buyerName + '\'' +
                ", buyerTel='" + buyerTel + '\'' +
                ", buyerAddr='" + buyerAddr + '\'' +
                ", buyerPostcode='" + buyerPostcode + '\'' +
                '}';
    }
}
