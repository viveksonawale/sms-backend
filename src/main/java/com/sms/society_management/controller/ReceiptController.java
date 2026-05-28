package com.sms.society_management.controller;

import com.sms.society_management.dto.response.ReceiptResponse;
import com.sms.society_management.service.PdfService;
import com.sms.society_management.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;
    private final PdfService pdfService;

    private static final DateTimeFormatter MONTH_FMT = DateTimeFormatter.ofPattern("MMMyyyy");

    @GetMapping("/{id}")
    public ResponseEntity<ReceiptResponse> getReceiptById(@PathVariable Long id) {
        return ResponseEntity.ok(receiptService.getReceiptById(id));
    }

    @GetMapping("/maintenance/{maintenanceId}")
    public ResponseEntity<ReceiptResponse> getReceiptByMaintenance(
            @PathVariable Long maintenanceId) {
        return ResponseEntity.ok(receiptService.getReceiptByMaintenance(maintenanceId));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadReceiptPdf(@PathVariable Long id) {
        ReceiptResponse receipt = receiptService.getReceiptById(id);
        byte[] pdfBytes = pdfService.generateReceiptPdf(id);

        // Build filename: OwnerName_ToMonth_FromMonth.pdf
        // e.g. RajeshSharma_May2026_April2026.pdf
        String safeName = receipt.getOwnerName()
                .replaceAll("[^a-zA-Z0-9]", ""); // strip spaces & special chars
        String toMonth   = receipt.getToDate().format(MONTH_FMT);
        String fromMonth = receipt.getFromDate().format(MONTH_FMT);
        String fileName  = safeName + "_" + toMonth + "_" + fromMonth + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentLength(pdfBytes.length);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReceipt(@PathVariable Long id) {
        receiptService.deleteReceipt(id);
        return ResponseEntity.noContent().build();
    }
}

