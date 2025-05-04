package com.fisa.card.card.service;

import com.fisa.card.bank.account.domain.Account;
import com.fisa.card.bank.account.repository.AccountRepository;
import com.fisa.card.card.controller.dto.req.CardRequest;
import com.fisa.card.card.controller.dto.res.CardResponse;
import com.fisa.card.card.domain.Card;
import com.fisa.card.card.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public CardResponse createCard(Long memberId, CardRequest req) {
        Account account = accountRepository.findByAccountNumber(req.getAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("해당 계좌번호로 등록된 계좌가 없습니다."));

        // 🔐 memberId 일치 검증
        if (!account.getMember().getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("해당 계좌는 회원에게 속하지 않습니다.");
        }

        Card card = Card.builder()
                .cardNumber(req.getCardNumber())
                .cardType(req.getCardType())
                .cardBIN(req.getCardBIN())
                .expiredAt(LocalDate.parse(req.getExpiredAt()))
                .cvv(req.getCvv())
                .account(account)
                .memberId(memberId)
                .build();

        Card saved = cardRepository.save(card);

        return CardResponse.builder()
                .cardId(saved.getCardId())
                .cardNumber(saved.getCardNumber())
                .cardType(saved.getCardType())
                .cardBIN(saved.getCardBIN())
                .expiredAt(saved.getExpiredAt().toString())
                .cvv(saved.getCvv())
                .build();
    }

}