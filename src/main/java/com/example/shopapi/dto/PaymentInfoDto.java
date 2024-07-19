package com.example.shopapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentInfoDto {

    @JsonProperty("imp_uid")
    private String impUid;

    @JsonProperty("paid_amount")
    private Long paidAmount;

    @JsonProperty("pay_method")
    private String payMethod;

    @JsonProperty("merchant_uid")
    private String merchantUid;

    private String name;

//    @JsonProperty("buyer_email")
//    private String buyerEmail;

    @JsonProperty("buyer_name")
    private String buyerName;

    @JsonProperty("buyer_tel")
    private String buyerTel;

    @JsonProperty("buyer_addr")
    private String buyerAddr;

    @JsonProperty("buyer_postcode")
    private String buyerPostcode;

    @Override
    public String toString() {
        return "PaymentInfoDto{" +
                "impUid='" + impUid + '\'' +
                ", paidAmount=" + paidAmount +
                ", payMethod='" + payMethod + '\'' +
                ", merchantUid='" + merchantUid + '\'' +
                ", name='" + name + '\'' +
//                ", buyerEmail='" + buyerEmail + '\'' +
                ", buyerName='" + buyerName + '\'' +
                ", buyerTel='" + buyerTel + '\'' +
                ", buyerAddr='" + buyerAddr + '\'' +
                ", buyerPostcode='" + buyerPostcode + '\'' +
                '}';
    }
}
