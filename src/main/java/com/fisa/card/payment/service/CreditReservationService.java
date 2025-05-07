package com.fisa.card.payment.service;



import com.fisa.card.bank.account.controller.dto.res.BankWithdrawResponse;
import com.fisa.card.payment.domain.CreditReservation;
import com.fisa.card.payment.repository.CreditReservationRepository;
import com.fisa.card.bank.account.controller.dto.req.BankWithdrawRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditReservationService {

    private final CreditReservationRepository creditReservationRepository;
    private final RestTemplate restTemplate;

    @Value("${bank.withdraw.url}")
    private String bankWithdrawUrl;

    @Value("${bank.withdraw.token}")
    private String bankServerToken;

    @Transactional
    public void processCreditReservations() {
        List<CreditReservation> reservations = creditReservationRepository.findByCharged(false);

        for (CreditReservation reservation : reservations) {
            BankWithdrawRequest request = new BankWithdrawRequest(
                    reservation.getCardNumber(), reservation.getAmount()
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", bankServerToken);

            HttpEntity<BankWithdrawRequest> httpEntity = new HttpEntity<>(request, headers);

            try {
                ResponseEntity<BankWithdrawResponse> response = restTemplate.exchange(
                        bankWithdrawUrl, HttpMethod.POST, httpEntity, BankWithdrawResponse.class);

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null
                        && "SUCCESS".equals(response.getBody().getStatus())) {
                    reservation.setCharged(true);
                    creditReservationRepository.save(reservation);
                    // 여기에 잔액 업데이트 등의 추가 작업도 가능
                } else {
                    // 실패 처리 로직
                    reservation.setCharged(false);
                    creditReservationRepository.save(reservation);
                }
            } catch (Exception e) {
                // 예외 처리 로직
                e.printStackTrace();
            }
        }
    }
}
