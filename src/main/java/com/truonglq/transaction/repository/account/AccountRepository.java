package com.truonglq.transaction.repository.account;

import com.truonglq.transaction.model.entities.Account;
import com.truonglq.transaction.service.account.CustomizedAccountRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String>, CustomizedAccountRepository {
    Account findByAccountNumber(String accountNumber);
}
