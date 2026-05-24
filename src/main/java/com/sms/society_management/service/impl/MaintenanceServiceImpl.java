package com.sms.society_management.service.impl;

import com.sms.society_management.dto.request.MaintenanceRequest;
import com.sms.society_management.dto.response.MaintenanceResponse;
import com.sms.society_management.entity.Maintenance;
import com.sms.society_management.entity.Owner;
import com.sms.society_management.entity.Receipt;
import com.sms.society_management.enums.PaymentStatus;
import com.sms.society_management.exception.DuplicateResourceException;
import com.sms.society_management.exception.ResourceNotFoundException;
import com.sms.society_management.mapper.MaintenanceMapper;
import com.sms.society_management.repository.MaintenanceRepository;
import com.sms.society_management.repository.OwnerRepository;
import com.sms.society_management.repository.ReceiptRepository;
import com.sms.society_management.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final OwnerRepository ownerRepository;
    private final ReceiptRepository receiptRepository;

    @Override
    @Transactional
    public MaintenanceResponse createMaintenance(MaintenanceRequest request) {
        log.info("Creating maintenance record for ownerId={}", request.getOwnerId());
        Owner owner = ownerRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Owner not found with id: " + request.getOwnerId()));

        Maintenance maintenance = MaintenanceMapper.toEntity(request, owner);
        Maintenance saved = maintenanceRepository.save(maintenance);
        log.info("Maintenance created with id={}", saved.getId());
        return MaintenanceMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public MaintenanceResponse getMaintenanceById(Long id) {
        log.debug("Fetching maintenance id={}", id);
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Maintenance not found with id: " + id));
        return MaintenanceMapper.toResponse(maintenance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceResponse> getMaintenanceByOwner(Long ownerId) {
        log.debug("Fetching maintenance for ownerId={}", ownerId);
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Owner not found with id: " + ownerId));
        return maintenanceRepository.findByOwner(owner).stream()
                .map(MaintenanceMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceResponse> getMaintenanceByStatus(PaymentStatus status) {
        log.debug("Fetching maintenance with status={}", status);
        return maintenanceRepository.findByStatus(status).stream()
                .map(MaintenanceMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public MaintenanceResponse markAsPaid(Long id, String transactionId) {
        log.info("Marking maintenance id={} as PAID with transactionId={}", id, transactionId);
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Maintenance not found with id: " + id));

        if (maintenance.getStatus() == PaymentStatus.PAID) {
            throw new DuplicateResourceException(
                    "Maintenance id=" + id + " is already marked as PAID");
        }

        maintenance.setStatus(PaymentStatus.PAID);
        maintenance.setPaymentDate(LocalDate.now());
        maintenance.setTransactionId(transactionId);
        Maintenance saved = maintenanceRepository.save(maintenance);

        // Generate receipt immediately after marking paid
        generateAndSaveReceipt(saved);

        log.info("Maintenance id={} marked PAID, receipt generated", id);
        return MaintenanceMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteMaintenance(Long id) {
        log.info("Deleting maintenance id={}", id);
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Maintenance not found with id: " + id));
        // Delete linked receipt first to avoid FK constraint violation
        receiptRepository.findByMaintenance(maintenance)
                .ifPresent(receiptRepository::delete);
        maintenanceRepository.delete(maintenance);
        log.info("Maintenance id={} deleted", id);
    }

    // ----------------------------------------------------------------
    // Internal: generate receipt number and persist receipt
    // Format: REC-01 (resets to 01 each new year)
    // ----------------------------------------------------------------
    private void generateAndSaveReceipt(Maintenance maintenance) {
        LocalDate startOfYear = maintenance.getPaymentDate().withDayOfYear(1);
        LocalDate endOfYear   = maintenance.getPaymentDate()
                .withDayOfYear(maintenance.getPaymentDate().lengthOfYear());

        long countThisYear = receiptRepository.countByGeneratedAtBetween(
                startOfYear.atStartOfDay(),
                endOfYear.atTime(23, 59, 59));

        String receiptNumber = String.format("REC-%03d", countThisYear + 1);

        Receipt receipt = Receipt.builder()
                .maintenance(maintenance)
                .receiptNumber(receiptNumber)
                .generatedAt(LocalDateTime.now())
                .build();

        receiptRepository.save(receipt);
        log.info("Receipt generated: {}", receiptNumber);
    }
}
