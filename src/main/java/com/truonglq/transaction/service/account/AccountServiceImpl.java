package com.truonglq.transaction.service.account;

import com.truonglq.transaction.dto.requests.AccountRegistrationRequest;
import com.truonglq.transaction.dto.responses.AccountRegistrationResponse;
import com.truonglq.transaction.model.entities.Account;
import com.truonglq.transaction.model.enums.AccountStatus;
import com.truonglq.transaction.repository.account.AccountRepository;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Random;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountServiceImpl implements AccountService {

    AccountRepository accountRepository;
    EntityManager em;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addAmount(String id, BigDecimal amount) {
        Account account = accountRepository.getAccountAndObtainPessimisticWriteLockOnItById(id);
        account.setBalance(account.getBalance().add(amount));
//        em.merge(account);
//        accountRepository.save(account);
    }

    @Override
    public AccountRegistrationResponse createAccount(AccountRegistrationRequest request) {
        Random random = new Random();
        int accountNumber = random.nextInt(900000000) + 100000000;
        String accountNumberString = String.valueOf(accountNumber);

        Account account = Account.builder()
                .accountNumber(accountNumberString)
                .name(request.getName())
                .balance(BigDecimal.ZERO)
                .status(AccountStatus.ACTIVE)
                .build();
        Account savedAccount = accountRepository.save(account);

        return AccountRegistrationResponse.builder()
                .accountNumber(savedAccount.getAccountNumber())
                .name(savedAccount.getName())
                .build();
    }
}
