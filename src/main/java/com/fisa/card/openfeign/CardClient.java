package com.fisa.card.openfeign;


import com.fisa.card.dto.req.BankWithdrawRequest;
import com.fisa.card.dto.res.BankWithdrawResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "Payment-service", url = "http://34.64.114.112:9090")
public interface CardClient {


    @PostMapping("/withdraw")
    BankWithdrawResponse withdrawFromBank(@RequestBody BankWithdrawRequest request);

}
