package com.fisa.card.card.service;

import com.fisa.card.card.controller.dto.res.OpaqueTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpaqueTokenService {

    private final RestTemplate restTemplate;

    @Value("${bank.auth.issue-url}")
    private String bankTokenUrl;

    public String issueOpaqueAuthToken(String clientId, String clientSecret) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 본문 생성
        HttpEntity<Map<String, String>> request = new HttpEntity<>(
                Map.of("clientId", clientId, "clientSecret", clientSecret),
                headers
        );


        ResponseEntity<OpaqueTokenResponse> response = restTemplate.exchange(
                bankTokenUrl,
                HttpMethod.POST,
                request,
                OpaqueTokenResponse.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().getOpaqueToken();
        }

        throw new IllegalStateException("은행 서버 인증 토큰 발급 실패");
    }
}
