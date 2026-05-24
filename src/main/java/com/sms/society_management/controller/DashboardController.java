package com.sms.society_management.controller;

import com.sms.society_management.dto.response.DashboardResponse;
import com.sms.society_management.enums.PaymentStatus;
import com.sms.society_management.repository.ExpenseRepository;
import com.sms.society_management.repository.MaintenanceRepository;
import com.sms.society_management.repository.OwnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;

@Slf4j
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final OwnerRepository ownerRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final ExpenseRepository expenseRepository;

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard() {
        log.debug("Building dashboard summary");

        YearMonth current = YearMonth.now();
        LocalDate start   = current.atDay(1);
        LocalDate end     = current.atEndOfMonth();

        DashboardResponse response = DashboardResponse.builder()
                .totalOwners(ownerRepository.count())
                .totalCollected(maintenanceRepository.sumAmountByStatus(PaymentStatus.PAID))
                .totalPending(maintenanceRepository.sumAmountByStatus(PaymentStatus.PENDING))
                .totalExpenses(expenseRepository.sumAllExpenses())
                .currentMonthCollection(maintenanceRepository.sumPaidAmountBetween(start, end))
                .currentMonthExpenses(expenseRepository.sumExpensesBetween(start, end))
                .build();

        return ResponseEntity.ok(response);
    }
}
