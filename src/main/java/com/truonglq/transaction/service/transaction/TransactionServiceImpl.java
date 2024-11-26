package com.truonglq.transaction.service.transaction;

import com.truonglq.transaction.dto.requests.TransactionRequest;
import com.truonglq.transaction.dto.responses.RecordTransactionResponse;
import com.truonglq.transaction.dto.responses.TransactionResponse;
import com.truonglq.transaction.model.entities.Account;
import com.truonglq.transaction.model.entities.Transaction;
import com.truonglq.transaction.model.enums.TransactionType;
import com.truonglq.transaction.repository.account.AccountRepository;
import com.truonglq.transaction.repository.transaction.TransactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    AccountRepository accountRepository;
    TransactionRepository transactionRepository;
    EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public TransactionResponse createTransaction(TransactionRequest request) {
        String senderId = request.getSenderAccountId();
        String receiverId = request.getReceiverAccountId();
        BigDecimal amount = request.getAmount();
        TransactionType type = request.getType();
        validateInputs(senderId, receiverId, amount);

//        Account sender = accountRepository.findById(senderId).orElseThrow(
//                () -> new IllegalArgumentException("Sender account not found")
//        );
//        Account receiver = accountRepository.findById(receiverId).orElseThrow(
//                () -> new IllegalArgumentException("Receiver account not found")
//        );
        Account sender = entityManager.find(Account.class, senderId, LockModeType.PESSIMISTIC_WRITE);
        if (sender == null) { throw new IllegalArgumentException("Sender account not found"); }
        Account receiver = entityManager.find(Account.class, receiverId, LockModeType.PESSIMISTIC_WRITE);
        if (receiver == null) { throw new IllegalArgumentException("Receiver account not found"); }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        BigDecimal senderOldBalance = sender.getBalance();
        BigDecimal receiverOldBalance = receiver.getBalance();

        processTransaction(sender, receiver, amount, type);

        Transaction transaction = Transaction.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .amount(amount)
                .type(type)
                .build();
        Transaction savedTransaction = transactionRepository.save(transaction);

        logger.info("Transaction created successfully: senderId={}, receiverId={}, amount={}, type={}",
                savedTransaction.getSenderId(),
                savedTransaction.getReceiverId(),
                savedTransaction.getAmount(),
                savedTransaction.getType()
        );

        return TransactionResponse.builder()
                .senderAccountNumber(sender.getAccountNumber())
                .receiverAccountNumber(receiver.getAccountNumber())
                .transactionId(savedTransaction.getId())
                .status("SUCCESS")
                .senderOldBalance(senderOldBalance)
                .receiverOldBalance(receiverOldBalance)
                .senderNewBalance(sender.getBalance())
                .receiverNewBalance(receiver.getBalance())
                .build();
    }

    private void validateInputs(String senderId, String receiverId, BigDecimal amount) {
        if (senderId.equals(receiverId)) { throw new IllegalArgumentException("Sender and receiver cannot be the same"); }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) { throw new IllegalArgumentException("Transaction amount must be greater than zero"); }
    }

    private void processTransaction(Account sender, Account receiver, BigDecimal amount, TransactionType type) {
        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));
//        if (type == TransactionType.WITHDRAWAL) {
//            sender.setBalance(sender.getBalance().subtract(amount));
//            receiver.setBalance(receiver.getBalance().add(amount));
//        } else {
//            sender.setBalance(sender.getBalance().add(amount));
//            receiver.setBalance(receiver.getBalance().subtract(amount));
//        }
        entityManager.persist(sender);
        entityManager.persist(receiver);
        entityManager.flush();
//        accountRepository.save(sender);
//        accountRepository.save(receiver);
    }

    @Override
    public List<RecordTransactionResponse> getTransactionsByUserId(String userId) {
        List<Transaction> sentTransactions = transactionRepository.findBySenderId(userId)
                .orElseThrow(() -> new IllegalArgumentException("No sent transactions found for user"));
        List<Transaction> receivedTransactions = transactionRepository.findByReceiverId(userId)
                .orElseThrow(() -> new IllegalArgumentException("No received transactions found for user"));

        List<Transaction> allTransactions = new ArrayList<>();
        allTransactions.addAll(sentTransactions);
        allTransactions.addAll(receivedTransactions);

        return allTransactions.stream()
                .map(transaction -> {
                    RecordTransactionResponse response = new RecordTransactionResponse();
                    response.setTransactionId(transaction.getId());
                    response.setAmount(transaction.getAmount());

                    if (transaction.getSenderId().equals(userId)) {
                        response.setUserId(transaction.getSenderId());
                        response.setRole("sender");
                    } else {
                        response.setUserId(transaction.getReceiverId());
                        response.setRole("receiver");
                    }

                    Account sender = accountRepository.findById(transaction.getSenderId())
                            .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));

                    Account receiver = accountRepository.findById(transaction.getReceiverId())
                            .orElseThrow(() -> new IllegalArgumentException("Receiver account not found"));

                    response.setSenderOldBalance(sender.getBalance()); // Before transaction
                    response.setReceiverOldBalance(receiver.getBalance()); // Before transaction

                    response.setSenderNewBalance(sender.getBalance());
                    response.setReceiverNewBalance(receiver.getBalance());

                    return response;
                })
                .collect(Collectors.toList());
    }
}
