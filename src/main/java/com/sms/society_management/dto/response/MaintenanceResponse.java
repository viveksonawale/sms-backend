package com.sms.society_management.dto.response;

import com.sms.society_management.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceResponse {
    private Long id;
    private Long ownerId;
    private String ownerName;
    private String flatNumber;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Integer numberOfMonths;   // auto-calculated — never from client
    private BigDecimal amount;
    private PaymentStatus status;
    private LocalDate paymentDate;
    private String transactionId;
}