package com.sms.society_management.controller;

import com.sms.society_management.dto.request.OwnerRequest;
import com.sms.society_management.dto.response.OwnerResponse;
import com.sms.society_management.service.OwnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @GetMapping
    public ResponseEntity<List<OwnerResponse>> getAllOwners() {
        return ResponseEntity.ok(ownerService.getAllOwners());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OwnerResponse> getOwnerById(@PathVariable Long id) {
        return ResponseEntity.ok(ownerService.getOwnerById(id));
    }

    @GetMapping("/flat/{flatNumber}")
    public ResponseEntity<OwnerResponse> getOwnerByFlatNumber(@PathVariable String flatNumber) {
        return ResponseEntity.ok(ownerService.getOwnerByFlatNumber(flatNumber));
    }

    @PostMapping
    public ResponseEntity<OwnerResponse> createOwner(@Valid @RequestBody OwnerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ownerService.createOwner(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OwnerResponse> updateOwner(
            @PathVariable Long id,
            @Valid @RequestBody OwnerRequest request) {
        return ResponseEntity.ok(ownerService.updateOwner(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable Long id) {
        ownerService.deleteOwner(id);
        return ResponseEntity.noContent().build();
    }
}
