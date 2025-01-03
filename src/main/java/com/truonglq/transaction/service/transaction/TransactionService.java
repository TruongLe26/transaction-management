package com.truonglq.transaction.service.transaction;

import com.truonglq.transaction.dto.requests.TransactionRequest;
import com.truonglq.transaction.dto.requests.TransactionRequest;
import com.truonglq.transaction.dto.responses.RecordTransactionResponse;
import com.truonglq.transaction.dto.responses.TransactionResponse;
import com.truonglq.transaction.dto.responses.TransactionResponse;
import com.truonglq.transaction.exception.AccountNotFoundException;
import com.truonglq.transaction.exception.InsufficientBalanceException;
import com.truonglq.transaction.exception.InvalidAmountException;
import com.truonglq.transaction.model.entities.Transaction;
import com.truonglq.transaction.model.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    TransactionResponse createTransaction(TransactionRequest request) throws AccountNotFoundException, InsufficientBalanceException, InvalidAmountException;
    List<RecordTransactionResponse> getTransactionsByUserId(String userId);
    Page<Transaction> getTransactions( String userId, String filter, Pageable pageable, BigDecimal minAmount, BigDecimal maxAmount, LocalDateTime startDate, LocalDateTime endDate);
    void addAmount(String id, BigDecimal amount);
    Page<Transaction> getTransactionsByNativeQuery(String userId, Pageable pageable);
//    List<Transaction> getPagedTransactions(int page, int pageSize);
}
