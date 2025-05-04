package com.fisa.card.bank.account.service;

import com.fisa.card.bank.account.controller.dto.req.AccountRequest;
import com.fisa.card.bank.account.controller.dto.res.AccountResponse;
import com.fisa.card.bank.account.domain.Account;
import com.fisa.card.bank.account.repository.AccountRepository;
import com.fisa.card.member.domain.Member;
import com.fisa.card.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;

    // 계좌 생성
    @Transactional
    public AccountResponse createAccount(Long memberId, AccountRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));   // 나중에 예외처리 리팩토일

        Account account = Account.builder()
                .accountNumber(request.getAccountNumber())
                .balance(request.getBalance())
                .status(request.getStatus())
                .member(member)
                .build();

        Account saved = accountRepository.save(account);

        return AccountResponse.builder()
                .accountId(saved.getAccountId())
                .accountNumber(saved.getAccountNumber())
                .balance(saved.getBalance())
                .status(saved.getStatus())
                .memberId(saved.getMember().getMemberId())
                .build();
    }
}
