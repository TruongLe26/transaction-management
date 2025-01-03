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
    Optional<List<Transaction>> findBySenderId(String senderId);
    Optional<List<Transaction>> findByReceiverId(String receiverId);

    @Query(value = "SELECT * FROM transaction t WHERE t.sender_id = :userId OR t.receiver_id = :userId LIMIT :limit OFFSET :offset",
            countQuery = "SELECT COUNT(*) FROM transaction t WHERE t.sender_id = :userId OR t.receiver_id = :userId",
            nativeQuery = true)
    Page<Transaction> findTransactionsByUserId(@Param("userId") String userId,
                                               @Param("limit") int limit,
                                               @Param("offset") int offset,
                                               Pageable pageable);
}
