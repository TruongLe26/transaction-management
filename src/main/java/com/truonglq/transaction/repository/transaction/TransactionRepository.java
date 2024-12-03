package com.truonglq.transaction.repository.transaction;

import com.truonglq.transaction.model.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction> {
    @Query("SELECT t FROM Transaction t WHERE t.senderId = :userId OR t.receiverId = :userId")
    Optional<List<Transaction>> findByUserId(@Param("userId") String userId);
    Optional<List<Transaction>> findBySenderId(String senderId);
    Optional<List<Transaction>> findByReceiverId(String receiverId);

    @Query("SELECT t FROM Transaction t WHERE t.senderId = :userId OR t.receiverId = :userId")
    Page<Transaction> findBySenderIdAndReceiverId(String userId, Pageable pageable);
    Page<Transaction> findBySenderId(String senderId, Pageable pageable);
    Page<Transaction> findByReceiverId(String receiverId, Pageable pageable);
}
