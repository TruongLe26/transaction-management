package com.truonglq.transaction.repository.account;

import com.truonglq.transaction.model.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByAccountNumber(String accountNumber);
}
