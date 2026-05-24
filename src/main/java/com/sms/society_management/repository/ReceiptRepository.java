package com.sms.society_management.repository;

import com.sms.society_management.entity.Maintenance;
import com.sms.society_management.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    Optional<Receipt> findByMaintenance(Maintenance maintenance);
    long countByGeneratedAtBetween(LocalDateTime start, LocalDateTime end);
}
