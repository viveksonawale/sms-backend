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

@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;
    private final PdfService pdfService;

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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(
                "attachment",
                "receipt-" + receipt.getReceiptNumber() + ".pdf");
        headers.setContentLength(pdfBytes.length);

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReceipt(@PathVariable Long id) {
        receiptService.deleteReceipt(id);
        return ResponseEntity.noContent().build();
    }
}
