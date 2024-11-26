package com.truonglq.transaction.service.account;

import com.truonglq.transaction.dto.requests.AccountRegistrationRequest;
import com.truonglq.transaction.dto.responses.AccountRegistrationResponse;
import com.truonglq.transaction.model.entities.Account;

public interface AccountService {
    AccountRegistrationResponse createAccount(AccountRegistrationRequest request);
}
