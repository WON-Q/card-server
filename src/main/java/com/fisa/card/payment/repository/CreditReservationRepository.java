package com.fisa.card.payment.repository;

import com.fisa.card.payment.domain.CreditReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditReservationRepository extends JpaRepository<CreditReservation, Long> {
}
