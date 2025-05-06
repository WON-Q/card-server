package com.fisa.card;

import com.fisa.card.bank.account.domain.Account;
import com.fisa.card.bank.account.repository.AccountRepository;
import com.fisa.card.card.domain.Card;
import com.fisa.card.card.domain.enums.CardType;
import com.fisa.card.card.repository.CardRepository;
import com.fisa.card.payment.controller.dto.req.PaymentRequest;
import com.fisa.card.payment.controller.dto.res.PaymentResultResponse;
import com.fisa.card.payment.repository.CreditReservationRepository;
import com.fisa.card.payment.repository.PaymentRepository;
import com.fisa.card.payment.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
@SpringBootTest
class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;

    @MockBean
    private PaymentRepository paymentRepository;

    @MockBean
    private CreditReservationRepository creditReservationRepository;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private CardRepository cardRepository;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setup() {
        mockServer = MockRestServiceServer.createServer(restTemplate); // 기존 restTemplate 사용
        // 💡 가짜 계좌 객체 생성
        Account account = Account.builder()
                .accountNumber("1234567890123456")
                .balance(10000L)
                .build();

        // 💡 가짜 카드 객체 생성
        Card card = Card.builder()
                .cardNumber("1234567890123456")
                .cardCvv("123")
                .cardLimit(5000L)
                .cardType(CardType.CHECK)
                .account(account)
                .build();

        // 💡 카드 리포지토리 동작 지정
        when(cardRepository.findByCardNumber("1234567890123456"))
                .thenReturn(Optional.of(card));

        mockServer.expect(requestTo("http://localhost:8083/api/bank/v1/withdraw"))
                .andExpect(method(org.springframework.http.HttpMethod.POST))
                .andRespond(withSuccess("""
                    {
                      "status": "SUCCESS",
                      "balance": 7000
                    }
                    """, MediaType.APPLICATION_JSON));

        when(paymentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void 체크카드_은행서버_연동테스트() {
        // 테스트용 가짜 카드/계좌 설정 생략 가능
        // 실제 paymentService.authorizePayment 내부에서 cardRepository.findByCardNumber(...) 호출 시 mock 결과도 필요

        PaymentResultResponse result = paymentService.authorizePayment("dummy-token", PaymentRequest.builder()
                .cardNumber("1234567890123456")
                .cvv("123")
                .expiryDate("12/30")
                .paymentId(1L)
                .amount(3000L)
                .requestedAt(LocalDateTime.now())
                .build());

        assertThat(result.getStatus()).isEqualTo("APPROVED");

        mockServer.verify(); // 이 시점에 요청이 정상적으로 갔는지 확인
    }


}
