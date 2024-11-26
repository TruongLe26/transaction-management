package com.truonglq.transaction;

import com.truonglq.transaction.dto.requests.TransactionRequest;
import com.truonglq.transaction.dto.responses.TransactionResponse;
import com.truonglq.transaction.model.entities.Account;
import com.truonglq.transaction.model.entities.Transaction;
import com.truonglq.transaction.model.enums.TransactionType;
import com.truonglq.transaction.repository.account.AccountRepository;
import com.truonglq.transaction.repository.transaction.TransactionRepository;
import com.truonglq.transaction.service.transaction.TransactionServiceImpl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransactionServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Account sender;
    private Account receiver;
    private TransactionRequest transactionRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sender = new Account();
        sender.setId("sender123");
        sender.setAccountNumber("ACC123");
        sender.setBalance(BigDecimal.valueOf(1000));

        receiver = new Account();
        receiver.setId("receiver456");
        receiver.setAccountNumber("ACC456");
        receiver.setBalance(BigDecimal.valueOf(500));

        transactionRequest = new TransactionRequest();
        transactionRequest.setSenderAccountId("sender123");
        transactionRequest.setReceiverAccountId("receiver456");
        transactionRequest.setAmount(BigDecimal.valueOf(100));
        transactionRequest.setType(TransactionType.DEPOSIT);
    }

    @Test
    void testCreateTransaction_Success() {
        when(entityManager.find(Account.class, "sender123")).thenReturn(sender);
        when(entityManager.find(Account.class, "receiver456")).thenReturn(receiver);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction("txn123", "sender123", "receiver456", BigDecimal.valueOf(100), TransactionType.DEPOSIT, null));

        TransactionResponse response = transactionService.createTransaction(transactionRequest);

        assertNotNull(response);
        assertEquals("ACC123", response.getSenderAccountNumber());
        assertEquals("ACC456", response.getReceiverAccountNumber());
        assertEquals("txn123", response.getTransactionId());
        assertEquals("SUCCESS", response.getStatus());
        assertEquals(BigDecimal.valueOf(900), response.getSenderNewBalance());
        assertEquals(BigDecimal.valueOf(600), response.getReceiverNewBalance());

        verify(entityManager).find(Account.class, "sender123");
        verify(entityManager).find(Account.class, "receiver456");
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_InsufficientBalance() {
        sender.setBalance(BigDecimal.valueOf(50));

        when(entityManager.find(Account.class, "sender123")).thenReturn(sender);
        when(entityManager.find(Account.class, "receiver456")).thenReturn(receiver);

        assertThrows(IllegalArgumentException.class, () -> transactionService.createTransaction(transactionRequest));
    }

    @Test
    void testCreateTransaction_SenderNotFound() {
        when(entityManager.find(Account.class, "sender123")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> transactionService.createTransaction(transactionRequest));
    }

    @Test
    void testCreateTransaction_ReceiverNotFound() {
        when(entityManager.find(Account.class, "sender123")).thenReturn(sender);
        when(entityManager.find(Account.class, "receiver456")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> transactionService.createTransaction(transactionRequest));
    }
}