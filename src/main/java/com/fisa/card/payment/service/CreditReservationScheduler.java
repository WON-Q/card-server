package com.fisa.card.payment.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CreditReservationScheduler {

    private final CreditReservationService creditReservationService;

    public CreditReservationScheduler(CreditReservationService creditReservationService) {
        this.creditReservationService = creditReservationService;
    }

    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void processCreditReservations() {
        creditReservationService.processCreditReservations();
    }
}
