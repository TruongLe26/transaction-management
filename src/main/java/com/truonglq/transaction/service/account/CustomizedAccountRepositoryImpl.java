package com.truonglq.transaction.service.account;

import com.truonglq.transaction.model.entities.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CustomizedAccountRepositoryImpl implements CustomizedAccountRepository {

    private final CustomizedAccountRepositoryContext customizedItemRepositoryContext;

    private final EntityManager em;

    @Override
    public void setLockTimeout(long timeoutDurationInMs) {
        customizedItemRepositoryContext.setLockTimeout(timeoutDurationInMs);
    }

    @Override
    public long getLockTimeout() {
        return customizedItemRepositoryContext.getLockTimeout();
    }

    @Override
    public Account getAccountAndObtainPessimisticWriteLockOnItById(String id) {
        log.info("Trying to obtain pessimistic lock ...");

        Query query = em.createQuery("select account from Account account where account.id = :id");
        query.setParameter("id", id);
        query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        query = customizedItemRepositoryContext.setLockTimeoutIfRequired(query);
        Account item = (Account) query.getSingleResult();

        log.info("... pessimistic lock obtained ...");

        customizedItemRepositoryContext.insertArtificialDealyAtTheEndOfTheQueryForTestsOnly();

        log.info("... pessimistic lock released.");

        return item;
    }
}
