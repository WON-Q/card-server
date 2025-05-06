package com.fisa.card.payment.repository;

import com.fisa.card.payment.domain.CreditReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditReservationRepository extends JpaRepository<CreditReservation, Long> {
    List<CreditReservation> findByCharged(boolean Charged);
}
