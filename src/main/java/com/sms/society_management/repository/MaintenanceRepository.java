package com.sms.society_management.repository;

import com.sms.society_management.entity.Maintenance;
import com.sms.society_management.entity.Owner;
import com.sms.society_management.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    List<Maintenance> findByOwner(Owner owner);
    List<Maintenance> findByStatus(PaymentStatus status);
}
