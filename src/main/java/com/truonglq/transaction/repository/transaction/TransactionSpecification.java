package com.truonglq.transaction.repository.transaction;

import com.truonglq.transaction.model.entities.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionSpecification {

    public static Specification<Transaction> hasUserId(String userId) {
        return (Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.equal(root.get("senderId"), userId),
                        criteriaBuilder.equal(root.get("receiverId"), userId)
                );
    }

    public static Specification<Transaction> isSender(String senderId) {
        return (Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("senderId"), senderId);
    }

    public static Specification<Transaction> isReceiver(String receiverId) {
        return (Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("receiverId"), receiverId);
    }

    public static Specification<Transaction> hasAmountGreaterThan(BigDecimal amount) {
        return (Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("amount"), amount);
    }

    public static Specification<Transaction> hasAmountLessThan(BigDecimal amount) {
        return (Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.lessThan(root.get("amount"), amount);
    }

    public static Specification<Transaction> createdBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
    }
}
