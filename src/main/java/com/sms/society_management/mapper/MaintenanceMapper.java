package com.sms.society_management.mapper;

import com.sms.society_management.dto.request.MaintenanceRequest;
import com.sms.society_management.dto.response.MaintenanceResponse;
import com.sms.society_management.entity.Maintenance;
import com.sms.society_management.entity.Owner;
import com.sms.society_management.enums.PaymentStatus;

import java.time.temporal.ChronoUnit;

public class MaintenanceMapper {

    /**
     * numberOfMonths is auto-calculated: inclusive count of months between fromDate and toDate.
     * e.g. April 2026 → April 2026 = 1 month
     *      April 2026 → June 2026  = 3 months
     * Admin only sends fromDate + toDate — numberOfMonths is derived, never from client input.
     */
    public static Maintenance toEntity(MaintenanceRequest request, Owner owner) {
        int months = (int) ChronoUnit.MONTHS.between(
                request.getFromDate().withDayOfMonth(1),
                request.getToDate().withDayOfMonth(1)) + 1;

        return Maintenance.builder()
                .owner(owner)
                .fromDate(request.getFromDate())
                .toDate(request.getToDate())
                .numberOfMonths(months)
                .amount(request.getAmount())
                .status(PaymentStatus.PENDING)
                .build();
    }

    public static MaintenanceResponse toResponse(Maintenance maintenance) {
        return MaintenanceResponse.builder()
                .id(maintenance.getId())
                .ownerId(maintenance.getOwner().getId())
                .ownerName(maintenance.getOwner().getName())
                .flatNumber(maintenance.getOwner().getFlatNumber())
                .fromDate(maintenance.getFromDate())
                .toDate(maintenance.getToDate())
                .numberOfMonths(maintenance.getNumberOfMonths())
                .amount(maintenance.getAmount())
                .status(maintenance.getStatus())
                .paymentDate(maintenance.getPaymentDate())
                .transactionId(maintenance.getTransactionId())
                .build();
    }
}
