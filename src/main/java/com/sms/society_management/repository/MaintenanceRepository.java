package com.sms.society_management.repository;

import com.sms.society_management.entity.Maintenance;
import com.sms.society_management.entity.Owner;
import com.sms.society_management.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {

    @Query("SELECT m FROM Maintenance m JOIN FETCH m.owner WHERE m.owner = :owner")
    List<Maintenance> findByOwner(@Param("owner") Owner owner);

    @Query("SELECT m FROM Maintenance m JOIN FETCH m.owner WHERE m.status = :status")
    List<Maintenance> findByStatus(@Param("status") PaymentStatus status);

    @Query("SELECT m FROM Maintenance m JOIN FETCH m.owner WHERE m.id = :id")
    Optional<Maintenance> findByIdWithOwner(@Param("id") Long id);

    @Query("SELECT COALESCE(SUM(m.amount), 0) FROM Maintenance m WHERE m.status = :status")
    BigDecimal sumAmountByStatus(@Param("status") PaymentStatus status);

    @Query("SELECT COALESCE(SUM(m.amount), 0) FROM Maintenance m WHERE m.status = 'PAID' AND m.paymentDate BETWEEN :start AND :end")
    BigDecimal sumPaidAmountBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
