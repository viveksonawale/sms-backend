package com.sms.society_management.entity;

import com.sms.society_management.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
    name = "maintenance",
    indexes = {
        @Index(name = "idx_maintenance_owner_id", columnList = "owner_id"),
        @Index(name = "idx_maintenance_status",   columnList = "status")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;

    @Column(name = "to_date", nullable = false)
    private LocalDate toDate;

    /**
     * Auto-calculated from fromDate → toDate (inclusive of both months).
     * e.g. April 2026 → June 2026 = 3 months.
     * Set by the mapper — never sent by client.
     */
    @Column(name = "number_of_months", nullable = false, columnDefinition = "integer default 1")
    private Integer numberOfMonths;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "transaction_id")
    private String transactionId;
}
