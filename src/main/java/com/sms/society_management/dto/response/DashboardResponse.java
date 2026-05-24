package com.sms.society_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private long totalOwners;
    private BigDecimal totalCollected;
    private BigDecimal totalPending;
    private BigDecimal totalExpenses;
    private BigDecimal currentMonthCollection;
    private BigDecimal currentMonthExpenses;
}
