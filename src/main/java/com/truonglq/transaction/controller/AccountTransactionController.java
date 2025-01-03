package com.truonglq.transaction.controller;

import com.truonglq.transaction.dto.requests.TransactionRequest;
import com.truonglq.transaction.dto.requests.TransactionRequest;
import com.truonglq.transaction.dto.responses.RecordTransactionResponse;
import com.truonglq.transaction.dto.responses.TransactionResponse;
import com.truonglq.transaction.dto.responses.TransactionResponse;
import com.truonglq.transaction.exception.AccountNotFoundException;
import com.truonglq.transaction.exception.InsufficientBalanceException;
import com.truonglq.transaction.exception.InvalidAmountException;
import com.truonglq.transaction.model.entities.Transaction;
import com.truonglq.transaction.service.transaction.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountTransactionController {

    TransactionService transactionService;

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    TransactionResponse transfer(@RequestBody TransactionRequest request) throws AccountNotFoundException, InsufficientBalanceException, InvalidAmountException {
        return transactionService.createTransaction(request);
    }

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    List<RecordTransactionResponse> getTransactionsByUserId(@PathVariable String userId) {
        return transactionService.getTransactionsByUserId(userId);
    }

    // Using Specification
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<Transaction> getTransactions(
            @RequestParam String userId,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable
    ) {
        return transactionService.getTransactions(userId, filter, pageable, minAmount, maxAmount, startDate, endDate);
    }

    // Using Keyset Pagination technique
//    @GetMapping("/paged")
//    @ResponseStatus(HttpStatus.OK)
//    List<Transaction> getPagedTransactions(
//            @RequestParam int page,
//            @RequestParam int pageSize
//    ) {
//        return transactionService.getPagedTransactions(page, pageSize);
//    }

    @GetMapping("/adv")
    @ResponseStatus(HttpStatus.OK)
    Page<Transaction> getTransactionsByNativeQuery(@RequestParam String userId, Pageable pageable) {
        return transactionService.getTransactionsByNativeQuery(userId, pageable);
    }
}
