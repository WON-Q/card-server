package com.fisa.card.bank.account.controller.dto.res;


import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankResponse {
    private String status;   // "SUCCESS" or "FAIL"
    private String reason;   // 실패 사유
    private Long balance;



}