package com.sms.society_management.mapper;

import com.sms.society_management.dto.request.MaintenanceRequest;
import com.sms.society_management.dto.response.MaintenanceResponse;
import com.sms.society_management.entity.Maintenance;
import com.sms.society_management.entity.Owner;
import com.sun.tools.javac.Main;

public class MaintenanceMapper {
    public static Maintenance toEntity(MaintenanceRequest request, Owner owner){
        return Maintenance.builder()
                .owner(owner)
                .fromDate(request.getFromDate())
                .toDate(request.getToDate())
                .amount(request.getAmount()).build();
    }
    public static MaintenanceResponse toResponse(Maintenance maintenance){
        return MaintenanceResponse.builder()
                .id(maintenance.getId())
                .ownerId(maintenance.getOwner().getId())
                .fromDate(maintenance.getFromDate())
                .toDate(maintenance.getToDate())
                .amount(maintenance.getAmount())
                .build();
    }
}
