package com.sms.society_management.service.impl;

import com.sms.society_management.dto.request.ExpenseRequest;
import com.sms.society_management.dto.response.ExpenseResponse;
import com.sms.society_management.entity.Expense;
import com.sms.society_management.exception.ResourceNotFoundException;
import com.sms.society_management.mapper.ExpenseMapper;
import com.sms.society_management.repository.ExpenseRepository;
import com.sms.society_management.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Override
    @Transactional
    public ExpenseResponse createExpense(ExpenseRequest request) {
        log.info("Creating expense: title={}, amount={}", request.getTitle(), request.getAmount());
        Expense expense = ExpenseMapper.toEntity(request);
        Expense saved = expenseRepository.save(expense);
        log.info("Expense created with id={}", saved.getId());
        return ExpenseMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseResponse getExpenseById(Long id) {
        log.debug("Fetching expense id={}", id);
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Expense not found with id: " + id));
        return ExpenseMapper.toResponse(expense);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getAllExpenses() {
        log.debug("Fetching all expenses");
        return expenseRepository.findAll().stream()
                .map(ExpenseMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getExpensesByDateRange(LocalDate start, LocalDate end) {
        log.debug("Fetching expenses between {} and {}", start, end);
        return expenseRepository.findByDateBetween(start, end).stream()
                .map(ExpenseMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ExpenseResponse updateExpense(Long id, ExpenseRequest request) {
        log.info("Updating expense id={}", id);
        Expense existing = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Expense not found with id: " + id));
        existing.setTitle(request.getTitle());
        existing.setCategory(request.getCategory());
        existing.setAmount(request.getAmount());
        existing.setDate(request.getDate());
        existing.setPaidTo(request.getPaidTo());
        existing.setNote(request.getNote());
        Expense updated = expenseRepository.save(existing);
        log.info("Expense id={} updated", id);
        return ExpenseMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteExpense(Long id) {
        log.info("Deleting expense id={}", id);
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Expense not found with id: " + id));
        expenseRepository.delete(expense);
        log.info("Expense id={} deleted", id);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getMonthlySummary(int year, int month) {
        log.debug("Computing expense summary for {}/{}", year, month);
        YearMonth ym = YearMonth.of(year, month);
        LocalDate start = ym.atDay(1);
        LocalDate end   = ym.atEndOfMonth();
        return expenseRepository.findByDateBetween(start, end).stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
