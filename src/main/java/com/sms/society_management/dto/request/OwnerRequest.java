package com.sms.society_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OwnerRequest {

    @NotBlank(message = "Owner name must not be blank")
    private String name;

    private String nameInMarathi;

    @NotBlank(message = "Flat number must not be blank")
    private String flatNumber;

    @NotBlank(message = "Phone number must not be blank")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must be a valid 10-digit Indian mobile number")
    private String phoneNo;
}
