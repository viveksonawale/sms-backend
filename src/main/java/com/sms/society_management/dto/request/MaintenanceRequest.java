package com.sms.society_management.dto.request;

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
public class MaintenanceRequest {
    private Long ownerId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private BigDecimal amount;
}
