package com.truonglq.transaction.controller;

import com.truonglq.transaction.dto.requests.TransactionRequest;
import com.truonglq.transaction.dto.requests.TransactionRequest;
import com.truonglq.transaction.dto.responses.RecordTransactionResponse;
import com.truonglq.transaction.dto.responses.TransactionResponse;
import com.truonglq.transaction.dto.responses.TransactionResponse;
import com.truonglq.transaction.service.transaction.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountTransactionController {

    TransactionService transactionService;

    @PostMapping("/transfer")
    TransactionResponse transfer(@RequestBody TransactionRequest request) {
        return transactionService.createTransaction(request);
    }

    @GetMapping("/user/{userId}")
    List<RecordTransactionResponse> getTransactionsByUserId(@PathVariable String userId) {
        return transactionService.getTransactionsByUserId(userId);
    }

}
