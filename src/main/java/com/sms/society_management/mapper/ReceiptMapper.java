package com.sms.society_management.mapper;

import com.sms.society_management.dto.response.ReceiptResponse;
import com.sms.society_management.entity.Receipt;

public class ReceiptMapper {
    public static ReceiptResponse toResponse(Receipt receipt){
        return ReceiptResponse.builder()
                .id(receipt.getId())
                .receiptNumber(receipt.getReceiptNumber())
                .generatedAt(receipt.getGeneratedAt())
                .ownerName(receipt.getMaintenance().getOwner().getName())
                .flatNumber(receipt.getMaintenance().getOwner().getFlatNumber())
                .fromDate(receipt.getMaintenance().getFromDate())
                .toDate(receipt.getMaintenance().getToDate())
                .amount(receipt.getMaintenance().getAmount())
                .build();
    }
}
