package com.fisa.card.payment.service;

import com.fisa.card.bank.account.controller.dto.req.BankWithdrawRequest;
import com.fisa.card.bank.account.controller.dto.res.BankResponse;
import com.fisa.card.bank.account.repository.AccountRepository;
import com.fisa.card.card.domain.Card;
import com.fisa.card.card.domain.enums.CardType;
import com.fisa.card.card.repository.CardRepository;
import com.fisa.card.payment.controller.dto.req.PaymentRequest;
import com.fisa.card.payment.controller.dto.res.PaymentResponse;
import com.fisa.card.payment.controller.dto.res.PaymentResultResponse;
import com.fisa.card.payment.domain.CreditReservation;
import com.fisa.card.payment.domain.Payment;
import com.fisa.card.payment.domain.enums.TransactionStatus;
import com.fisa.card.payment.repository.CreditReservationRepository;
import com.fisa.card.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final CreditReservationRepository creditReservationRepository;
    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final RestTemplate restTemplate;

    @Value("${bank.withdraw.url}")
    private String bankWithdrawUrl;

    @Value("${bank.withdraw.token}")
    private String bankServerToken;

    @Transactional
    public PaymentResultResponse authorizePayment(String jwtToken, PaymentRequest request) {
        // 1. 카드 조회 및 유효성 검사
        Card card = cardRepository.findByCardNumber(request.getCardNumber())
                .orElseThrow(() -> new IllegalArgumentException("해당 카드번호가 존재하지 않습니다."));

        YearMonth expiry = YearMonth.parse(request.getExpiryDate(), DateTimeFormatter.ofPattern("MM/yy"));
        if (expiry.isBefore(YearMonth.now())) {
            throw new IllegalArgumentException("카드 유효기간이 만료되었습니다.");
        }

        if (!card.getCardCvv().equals(request.getCvv())) {
            throw new IllegalArgumentException("CVV 번호가 일치하지 않습니다.");
        }

        if (request.getAmount() > card.getCardLimit()) {
            throw new IllegalArgumentException("카드 한도를 초과했습니다.");
        }

        // 2. 결제 내역 저장 (PENDING)
        Payment payment = Payment.builder()
                .paymentId(request.getPaymentId())
                .amount(request.getAmount())
                .cardType(card.getCardType())
                .cardNumber(request.getCardNumber())
                .expiryDate(request.getExpiryDate())
                .cvv(request.getCvv())
                .requestedAt(request.getRequestedAt())
                .transactionStatus(TransactionStatus.PENDING)
                .build();

        Payment saved = paymentRepository.save(payment);

        // 3. 카드 타입 분기 처리
        try {
            if (card.getCardType() == CardType.CHECK) {
                // 체크카드 처리
                BankWithdrawRequest bankReq = new BankWithdrawRequest(
                        card.getAccount().getAccountNumber(),
                        request.getAmount()
                );

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", bankServerToken);

                HttpEntity<BankWithdrawRequest> httpEntity = new HttpEntity<>(bankReq, headers);

                ResponseEntity<BankResponse> response = restTemplate.exchange(
                        bankWithdrawUrl,
                        HttpMethod.POST,
                        httpEntity,
                        BankResponse.class
                );
                    /*
                      응답 성공했을떄
                     */
                if (response.getStatusCode().is2xxSuccessful()
                        && response.getBody() != null
                        && "SUCCESS".equals(response.getBody().getStatus())) {

                    saved.setTransactionStatus(TransactionStatus.SUCCESS);
                    saved.setCompleteAt(LocalDateTime.now());

                    Long updatedBalance = response.getBody().getBalance();
                    card.getAccount().setBalance(updatedBalance);
                    accountRepository.save(card.getAccount());

                    return buildResponse(saved, "APPROVED", "Payment authorized");
                } else {
                    log.warn("체크카드 결제 실패: {}",
                            response.getBody() != null ? response.getBody().getReason() : "응답 없음");
                    saved.setTransactionStatus(TransactionStatus.FAILURE);
                    saved.setCompleteAt(LocalDateTime.now());
                    return buildResponse(saved, "FAILED", "결제 실패");
                }

            } else if (card.getCardType() == CardType.CREDIT) {
                // 신용카드 처리 (예약 저장)
                saved.setTransactionStatus(TransactionStatus.SUCCESS);
                saved.setCompleteAt(LocalDateTime.now());

                CreditReservation reservation = CreditReservation.builder()
                        .paymentId(saved.getPaymentId())
                        .cardNumber(saved.getCardNumber())
                        .amount(saved.getAmount())
                        .reservedAt(LocalDateTime.now())
                        .charged(false)
                        .build();

                creditReservationRepository.save(reservation);

                return buildResponse(saved, "APPROVED", "Payment authorized");
            }

        } catch (Exception e) {
            log.error("결제 처리 중 예외 발생", e);
            saved.setTransactionStatus(TransactionStatus.FAILURE);
            return buildResponse(saved, "FAILED", "결제 처리 중 오류가 발생했습니다.");
        }

        // 기타 예외 처리
        saved.setTransactionStatus(TransactionStatus.FAILURE);
        saved.setCompleteAt(LocalDateTime.now());
        return buildResponse(saved, "FAILED", "지원되지 않는 카드 타입입니다.");
    }


    private PaymentResultResponse buildResponse(Payment payment, String status, String message) {
        return PaymentResultResponse.builder()
                .authorizationId("AUTH" + payment.getTxnId())
                .status(status)
                .message(message)
                .build();
    }



    public List<PaymentResponse> getCompletedPayments() {
        List<Payment> payments = paymentRepository.findByTransactionStatusIn(List.of(
                TransactionStatus.SUCCESS,
                TransactionStatus.FAILURE
        ));

        return payments.stream()
                .map(payment -> PaymentResponse.builder()
                        .authorizationId("AUTH" + payment.getTxnId())
                        .status(payment.getTransactionStatus().name()) // SUCCESS or FAILURE
                        .amount(payment.getAmount())
                        .cardType(payment.getCardType())
                        .cardNumber(payment.getCardNumber())
                        .merchant(payment.getMerchant())
                        .build()
                ).toList();
    }


}
