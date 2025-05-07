package com.fisa.card.bank.account.controller.dto.res;


import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankWithdrawResponse {
    private String account;
    private Long amount;
    private String status;
    private Long balance;


}