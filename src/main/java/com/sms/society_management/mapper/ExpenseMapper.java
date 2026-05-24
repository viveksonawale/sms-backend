package com.sms.society_management.mapper;

import com.sms.society_management.dto.request.ExpenseRequest;
import com.sms.society_management.dto.response.ExpenseResponse;
import com.sms.society_management.entity.Expense;

public class ExpenseMapper {
    public static Expense toEntity(ExpenseRequest request){
        return Expense.builder()
                .title(request.getTitle())
                .category(request.getCategory())
                .amount(request.getAmount())
                .date(request.getDate())
                .paidTo(request.getPaidTo())
                .note(request.getNote())
                .build();
    }

    public static ExpenseResponse toResponse(Expense expense){
        return ExpenseResponse.builder()
                .id(expense.getId())
                .title(expense.getTitle())
                .category(expense.getCategory())
                .amount(expense.getAmount())
                .date(expense.getDate())
                .paidTo(expense.getPaidTo())
                .note(expense.getNote())
                .build();
    }
}
