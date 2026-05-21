package com.sms.society_management.mapper;

import com.sms.society_management.dto.request.OwnerRequest;
import com.sms.society_management.dto.response.OwnerResponse;
import com.sms.society_management.entity.Owner;

public class OwnerMapper {
    public static Owner toEntity(OwnerRequest request) {
        return Owner.builder()
                .name(request.getName())
                .nameInMarathi(request.getNameInMarathi())
                .flatNumber(request.getFlatNumber())
                .phoneNo(request.getPhoneNo())
                .build();
    }
    public static OwnerResponse toResponse(Owner owner) {
        return OwnerResponse.builder()
                .id(owner.getId())
                .name(owner.getName())
                .nameInMarathi(owner.getNameInMarathi())
                .flatNumber(owner.getFlatNumber())
                .phoneNo(owner.getPhoneNo())
                .build();
    }
}
