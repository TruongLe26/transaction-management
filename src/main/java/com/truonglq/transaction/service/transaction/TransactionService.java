package com.truonglq.transaction.service.transaction;

import com.truonglq.transaction.dto.requests.TransactionRequest;
import com.truonglq.transaction.dto.requests.TransactionRequest;
import com.truonglq.transaction.dto.responses.RecordTransactionResponse;
import com.truonglq.transaction.dto.responses.TransactionResponse;
import com.truonglq.transaction.dto.responses.TransactionResponse;
import com.truonglq.transaction.model.entities.Transaction;
import com.truonglq.transaction.model.enums.TransactionType;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    TransactionResponse createTransaction(TransactionRequest request);
    List<RecordTransactionResponse> getTransactionsByUserId(String userId);
}
