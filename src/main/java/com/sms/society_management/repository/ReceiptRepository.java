package com.sms.society_management.repository;

import com.sms.society_management.entity.Maintenance;
import com.sms.society_management.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    Optional<Receipt> findByMaintenance(Maintenance maintenance);
}
