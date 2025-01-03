//package com.truonglq.transaction.repository.transaction;
//
//import com.blazebit.persistence.CriteriaBuilderFactory;
//import com.blazebit.persistence.PagedList;
//import com.truonglq.transaction.model.entities.Transaction;
//import jakarta.persistence.EntityManager;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//@Repository
//@Transactional
//@RequiredArgsConstructor
//public class CustomizedTransactionRepositoryImpl implements CustomizedTransactionRepository {
//
//    private final EntityManager em;
//    private final CriteriaBuilderFactory cbf;
//
//    @Override
//    public PagedList<Transaction> findTransactionsPaged(int page, int pageSize) {
//        PagedList<Transaction> postPage = cbf.create(em, Transaction.class, "t")
//                .orderByAsc("t.createdAt")
//                .orderByAsc("t.id")
//                .page(0, pageSize)
//                .withKeysetExtraction(true)
//                .getResultList();
//
//        if (page == 1) { return postPage; }
//        return cbf.create(em, Transaction.class, "t")
//                .orderByAsc("t.createdAt")
//                .orderByAsc("t.id")
//                .page(postPage.getKeysetPage(), page, pageSize)
//                .getResultList();
//    }
//}
