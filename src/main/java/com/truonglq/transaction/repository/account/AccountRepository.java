package com.truonglq.transaction.repository.account;

import com.truonglq.transaction.model.entities.Account;
import com.truonglq.transaction.service.account.CustomizedAccountRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String>, CustomizedAccountRepository {
    Account findByAccountNumber(String accountNumber);
    Optional<Account> findByName(String accountName);
}
