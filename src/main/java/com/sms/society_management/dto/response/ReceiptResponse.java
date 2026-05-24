package com.sms.society_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptResponse {
    private Long id;
    private String receiptNumber;
    private LocalDateTime generatedAt;

    private String ownerName;
    private String flatNumber;

    private LocalDate fromDate;
    private LocalDate toDate;

    private BigDecimal amount;
}
