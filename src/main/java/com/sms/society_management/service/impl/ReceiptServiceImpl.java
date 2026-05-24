package com.sms.society_management.service.impl;

import com.sms.society_management.dto.response.ReceiptResponse;
import com.sms.society_management.entity.Maintenance;
import com.sms.society_management.entity.Receipt;
import com.sms.society_management.exception.ResourceNotFoundException;
import com.sms.society_management.mapper.ReceiptMapper;
import com.sms.society_management.repository.MaintenanceRepository;
import com.sms.society_management.repository.ReceiptRepository;
import com.sms.society_management.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final MaintenanceRepository maintenanceRepository;

    @Override
    @Transactional(readOnly = true)
    public ReceiptResponse generateReceipt(Long maintenanceId) {
        // This variant is exposed on the interface for on-demand retrieval
        // Actual generation happens inside MaintenanceServiceImpl.markAsPaid()
        return getReceiptByMaintenance(maintenanceId);
    }

    @Override
    @Transactional(readOnly = true)
    public ReceiptResponse getReceiptById(Long id) {
        log.debug("Fetching receipt id={}", id);
        Receipt receipt = receiptRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Receipt not found with id: " + id));
        return ReceiptMapper.toResponse(receipt);
    }

    @Override
    @Transactional(readOnly = true)
    public ReceiptResponse getReceiptByMaintenance(Long maintenanceId) {
        log.debug("Fetching receipt for maintenanceId={}", maintenanceId);
        Maintenance maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Maintenance not found with id: " + maintenanceId));
        Receipt receipt = receiptRepository.findByMaintenance(maintenance)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No receipt found for maintenance id: " + maintenanceId));
        return ReceiptMapper.toResponse(receipt);
    }

    @Override
    @Transactional
    public void deleteReceipt(Long id) {
        log.info("Deleting receipt id={}", id);
        Receipt receipt = receiptRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Receipt not found with id: " + id));
        // Reset maintenance back to PENDING so it can be re-recorded
        Maintenance maintenance = receipt.getMaintenance();
        maintenance.setStatus(com.sms.society_management.enums.PaymentStatus.PENDING);
        maintenance.setPaymentDate(null);
        maintenance.setTransactionId(null);
        maintenanceRepository.save(maintenance);
        receiptRepository.delete(receipt);
        log.info("Receipt id={} deleted, maintenance id={} reset to PENDING", id, maintenance.getId());
    }
}
