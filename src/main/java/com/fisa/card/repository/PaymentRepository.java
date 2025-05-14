package com.fisa.card.repository;



import com.fisa.card.entity.Payment;
import com.fisa.card.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.cardNumber = :cardNumber AND p.charged = false AND p.paymentStatus = 'SUCCESS'")
    Long findUnchargedTotalByCardNumber(@Param("cardNumber") String cardNumber);

}