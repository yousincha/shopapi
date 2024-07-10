package com.example.shopapi.config;
import com.example.shopapi.domain.PaymentInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
@Component
public class IamportClient {

    private static final Logger logger = LoggerFactory.getLogger(IamportClient.class);

    @Value("${iamport.api.key}")
    private String apiKey;

    @Value("${iamport.api.secret}")
    private String apiSecret;

    private final RestTemplate restTemplate;

    public IamportClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PaymentInfo retrievePayment(String impUid) {
        // 로그 추가
        logger.info("API Key: {}", apiKey);
        logger.info("API Secret: {}", apiSecret);

        // 아임포트 API를 호출하여 결제 정보 조회
        String url = "https://api.iamport.kr/payments/" + impUid;
        return restTemplate.getForObject(url, PaymentInfo.class);
    }
}