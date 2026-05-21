package com.sms.society_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OwnerResponse {
    private Long id;
    private String name;
    private String nameInMarathi;
    private String flatNumber;
    private String phoneNo;
}