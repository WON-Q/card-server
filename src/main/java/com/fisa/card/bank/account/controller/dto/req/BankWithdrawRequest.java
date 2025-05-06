package com.fisa.card.bank.account.controller.dto.req;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankWithdrawRequest {
    private String account;
    private Long amount;


}