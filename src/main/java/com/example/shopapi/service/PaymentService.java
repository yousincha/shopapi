package com.example.shopapi.service;

import com.example.shopapi.domain.PaymentInfo;
import com.example.shopapi.repository.PaymentInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

import java.util.Map;

@Service
public class PaymentService {

    private final PaymentInfoRepository paymentInfoRepository;

    @Autowired
    public PaymentService(PaymentInfoRepository paymentInfoRepository) {
        this.paymentInfoRepository = paymentInfoRepository;
    }

    public ResponseEntity<?> verifyIamportPayment(@PathVariable String impUid, @RequestParam Map<String, String> requestData) {
        try {

            // requestData에서 필요한 정보를 추출합니다.
            Long paidAmount = requestData.get("paid_amount") != null ? Long.valueOf(requestData.get("paid_amount").toString()) : null;

            // null 체크 후 기본값 설정
            if (paidAmount == null) {
                return ResponseEntity.badRequest().body("Payment failed: Invalid paid_amount");
            }

            // 결제 정보를 저장합니다.
            PaymentInfo paymentInfo = new PaymentInfo();
            paymentInfo.setImpUid(impUid);
            paymentInfo.setPaidAmount(paidAmount);
            paymentInfo.setPayMethod(requestData.get("pay_method") != null ? requestData.get("pay_method").toString() : null);
            paymentInfo.setMerchantUid(requestData.get("merchant_uid") != null ? requestData.get("merchant_uid").toString() : null);
            paymentInfo.setName (requestData.get("name") != null ? requestData.get("name").toString() : null);
//            paymentInfo.setBuyerEmail(requestData.get("buyer_email") != null ? requestData.get("buyer_email").toString() : null);
            paymentInfo.setBuyerName(requestData.get("buyer_name") != null ? requestData.get("buyer_name").toString() : null);
            paymentInfo.setBuyerTel(requestData.get("buyer_tel") != null ? requestData.get("buyer_tel").toString() : null);
            paymentInfo.setBuyerAddr(requestData.get("buyer_addr") != null ? requestData.get("buyer_addr").toString() : null);
            paymentInfo.setBuyerPostcode(requestData.get("buyer_postcode") != null ? requestData.get("buyer_postcode").toString() : null);

            paymentInfoRepository.save(paymentInfo);

            // 결제된 금액이 적절한지 확인
            // 필요에 따라 추가 검증 로직을 추가할 수 있습니다.

            return ResponseEntity.ok(Map.of("paid_amount", paidAmount, "message", "Payment succeeded"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while verifying payment");
        }
    }
    public ResponseEntity<?> getAllPaymentsInfos() {
        try {
            List<PaymentInfo> paymentInfos = paymentInfoRepository.findAll();
            return ResponseEntity.ok(paymentInfos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while fetching payment infos");
        }
    }
}
