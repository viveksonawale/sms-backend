package com.sms.society_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "receipt",
    indexes = {
        @Index(name = "idx_receipt_maintenance_id", columnList = "maintenance_id", unique = true)
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maintenance_id", nullable = false, unique = true)
    private Maintenance maintenance;

    @Column(name = "receipt_number", nullable = false)
    private String receiptNumber;

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt;
}
