package com.sms.society_management.controller;

import com.sms.society_management.dto.request.MaintenanceRequest;
import com.sms.society_management.dto.response.MaintenanceResponse;
import com.sms.society_management.enums.PaymentStatus;
import com.sms.society_management.service.MaintenanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @PostMapping
    public ResponseEntity<MaintenanceResponse> createMaintenance(
            @Valid @RequestBody MaintenanceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(maintenanceService.createMaintenance(request));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<MaintenanceResponse>> getByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(maintenanceService.getMaintenanceByOwner(ownerId));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<MaintenanceResponse>> getPending() {
        return ResponseEntity.ok(maintenanceService.getMaintenanceByStatus(PaymentStatus.PENDING));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceResponse> getMaintenanceById(@PathVariable Long id) {
        return ResponseEntity.ok(maintenanceService.getMaintenanceById(id));
    }

    @PatchMapping("/{id}/pay")
    public ResponseEntity<MaintenanceResponse> markAsPaid(
            @PathVariable Long id,
            @RequestParam(required = false) String transactionId) {
        return ResponseEntity.ok(maintenanceService.markAsPaid(id, transactionId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable Long id) {
        maintenanceService.deleteMaintenance(id);
        return ResponseEntity.noContent().build();
    }
}
