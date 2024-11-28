package com.truonglq.transaction.service.account;

import com.truonglq.transaction.dto.requests.AccountRegistrationRequest;
import com.truonglq.transaction.dto.responses.AccountRegistrationResponse;
import com.truonglq.transaction.model.entities.Account;

import java.math.BigDecimal;

public interface AccountService {
    void addAmount(String id, BigDecimal amount);
    AccountRegistrationResponse createAccount(AccountRegistrationRequest request);
}
