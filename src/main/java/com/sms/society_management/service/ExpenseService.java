package com.sms.society_management.service;

import com.sms.society_management.dto.request.ExpenseRequest;
import com.sms.society_management.dto.response.ExpenseResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {
    ExpenseResponse createExpense(ExpenseRequest request);
    ExpenseResponse getExpenseById(Long id);
    List<ExpenseResponse> getAllExpenses();
    List<ExpenseResponse> getExpensesByDateRange(LocalDate start, LocalDate end);
    ExpenseResponse updateExpense(Long id, ExpenseRequest request);
    void deleteExpense(Long id);
    BigDecimal getMonthlySummary(int year, int month);
}
