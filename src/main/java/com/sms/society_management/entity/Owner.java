package com.sms.society_management.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "owner",
    indexes = {
        @Index(name = "idx_owner_flat_number", columnList = "flat_number", unique = true)
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "name_in_marathi")
    private String nameInMarathi;

    @Column(name = "flat_number", nullable = false, unique = true)
    private String flatNumber;

    @Column(name = "phone_no", nullable = false, unique = true)
    private String phoneNo;
}
