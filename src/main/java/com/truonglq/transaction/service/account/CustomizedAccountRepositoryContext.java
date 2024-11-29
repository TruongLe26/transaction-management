package com.truonglq.transaction.service.account;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class CustomizedAccountRepositoryContext {

    @Getter
    @Value("${concurrency.pessimisticLocking.requiredToSetLockTimeoutForTestsAtStartup: false}")
    private boolean requiredToSetLockTimeoutForTestsAtStartup;

    @Value("${concurrency.pessimisticLocking.requiredToSetLockTimeoutForEveryQuery: true}")
    private boolean requiredToSetLockTimeoutForEveryQuery;

    @Getter
    @Value("${concurrency.pessimisticLocking.requiredToSetLockTimeoutQueryHint: false}")
    private boolean requiredToSetLockTimeoutQueryHint;

    @Getter
    @Value("${concurrency.pessimisticLocking.delayAtTheEndOfTheQueryForPessimisticLockingTestingInMs: 2000}")
    private long delayAtTheEndOfTheQueryForPessimisticLockingTestingInMs;

    @Getter
    @Value("${concurrency.pessimisticLocking.minimalPossibleLockTimeOutInMs: 1000}")
    private long minimalPossibleLockTimeOutInMs;

    @Getter
    @Value("${concurrency.pessimisticLocking.lockTimeOutInMsForQueryGetAccount: 5000}")
    private long lockTimeOutInMsForQueryGetAccount;

    protected final EntityManager em;

    protected void setLockTimeout(long timeoutDurationInMs) {
        long timeoutDurationInSec = TimeUnit.MILLISECONDS.toSeconds(timeoutDurationInMs);
        Query query = em.createNativeQuery("set session innodb_lock_wait_timeout = " + timeoutDurationInSec);
        query.executeUpdate();
    }

    protected long getLockTimeout() {
        Query query = em.createNativeQuery("select @@innodb_lock_wait_timeout");
//        long timeoutDurationInSec = ((BigInteger) query.getSingleResult()).longValue();
//        return TimeUnit.SECONDS.toMillis(timeoutDurationInSec);
        Object result = query.getSingleResult();
        if (result instanceof Number) {
            long timeoutDurationInSec = ((Number) result).longValue();
            return TimeUnit.SECONDS.toMillis(timeoutDurationInSec);
        } else {
            throw new IllegalStateException("Unexpected type for innodb_lock_wait_timeout: " + result.getClass().getName());
        }
    }

    protected Query setLockTimeoutIfRequired(Query query) {
        if (requiredToSetLockTimeoutForEveryQuery) {
            log.info("... set lockTimeOut {} ms through native query ...", getLockTimeOutInMsForQueryGetAccount());
            setLockTimeout(getLockTimeOutInMsForQueryGetAccount());
        }

        if (requiredToSetLockTimeoutQueryHint) {
            log.info("... set lockTimeOut {} ms through query hint ...", getLockTimeOutInMsForQueryGetAccount());
            query.setHint("javax.persistence.lock.timeout", String.valueOf(getLockTimeOutInMsForQueryGetAccount()));
        }

        return query;
    }

//    protected to public
    public void insertArtificialDealyAtTheEndOfTheQueryForTestsOnly() {
        // for testing purposes only
    }
}
