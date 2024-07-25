package com.example.shopapi.controller;

import com.example.shopapi.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paymentInfos")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{impUid}")
    public ResponseEntity<?> verifyIamportPayment(@PathVariable String impUid, @RequestParam Map<String, String> params) {
        System.out.println("Received request with impUid: " + impUid);
        System.out.println("Request Params: " + params);
        return paymentService.verifyIamportPayment(impUid, params);
    }
    @GetMapping
    public ResponseEntity<?> getAllPaymentsInfos() {
        return paymentService.getAllPaymentsInfos();
    }

}
