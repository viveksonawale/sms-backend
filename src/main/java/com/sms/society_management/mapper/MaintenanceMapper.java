package com.sms.society_management.mapper;

import com.sms.society_management.dto.request.MaintenanceRequest;
import com.sms.society_management.entity.Maintenance;

public class MaintenanceMapper {
    public static Maintenance toEntity(MaintenanceRequest maintenanceRequest){
        return Maintenance.builder()
                .build();
    }
}
