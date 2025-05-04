package com.fisa.card.card.repository;


import com.fisa.card.card.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}