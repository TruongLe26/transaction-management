package com.truonglq.transaction.service.account;

import com.truonglq.transaction.model.entities.Account;

public interface CustomizedAccountRepository {
    void setLockTimeout(long timeoutDurationInMs);

    long getLockTimeout();

    Account getAccountAndObtainPessimisticWriteLockOnItById(String id);
}
