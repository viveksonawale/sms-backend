package com.sms.society_management.service;

public interface PdfService {
    byte[] generateReceiptPdf(Long receiptId);
}
