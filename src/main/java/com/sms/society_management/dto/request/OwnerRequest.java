package com.sms.society_management.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OwnerRequest {
    private String name;
    private String nameInMarathi;
    private String flatNumber;
    private String phoneNo;
}
