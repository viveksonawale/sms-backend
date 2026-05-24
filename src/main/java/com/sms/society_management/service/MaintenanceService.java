package com.sms.society_management.service;

import com.sms.society_management.dto.request.MaintenanceRequest;
import com.sms.society_management.dto.response.MaintenanceResponse;
import com.sms.society_management.enums.PaymentStatus;

import java.util.List;

public interface MaintenanceService {
    MaintenanceResponse createMaintenance(MaintenanceRequest request);
    MaintenanceResponse getMaintenanceById(Long id);
    List<MaintenanceResponse> getMaintenanceByOwner(Long ownerId);
    List<MaintenanceResponse> getMaintenanceByStatus(PaymentStatus status);
    MaintenanceResponse markAsPaid(Long id, String transactionId);  // important business logic
    void deleteMaintenance(Long id);
}
