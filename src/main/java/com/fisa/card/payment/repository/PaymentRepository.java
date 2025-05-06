package com.fisa.card.payment.repository;


import com.fisa.card.payment.domain.Payment;
import com.fisa.card.payment.domain.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByTransactionStatusIn(List<TransactionStatus> statuses);

}