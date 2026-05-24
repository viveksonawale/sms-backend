package com.sms.society_management.service;

import com.sms.society_management.dto.response.ReceiptResponse;

public interface ReceiptService {
    ReceiptResponse generateReceipt(Long maintenanceId);
    ReceiptResponse getReceiptById(Long id);
    ReceiptResponse getReceiptByMaintenance(Long maintenanceId);
}