package com.fisa.card.openfeign;


import com.fisa.card.dto.req.BankWithdrawRequest;
import com.fisa.card.dto.res.BankWithdrawResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "Payment-service", url = "${bank.withdraw.url}")
public interface CardClient {


    @PostMapping("/withdraw")
    BankWithdrawResponse withdrawFromBank(@RequestBody BankWithdrawRequest request);


    @PostMapping("/deposit")
    BankWithdrawResponse depositToBank(@RequestBody BankWithdrawRequest request);

}
