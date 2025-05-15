package com.fisa.card.repository;

import com.fisa.card.entity.BinInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BinInfoRepository extends JpaRepository<BinInfo, String> {
    Optional<BinInfo> findByBin(String bin);
}
