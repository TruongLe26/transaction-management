package com.truonglq.transaction.account;

import com.truonglq.transaction.dto.requests.AccountRegistrationRequest;
import com.truonglq.transaction.dto.responses.AccountRegistrationResponse;
import com.truonglq.transaction.model.entities.Account;
import com.truonglq.transaction.model.enums.AccountStatus;
import com.truonglq.transaction.repository.account.AccountRepository;
import com.truonglq.transaction.service.account.AccountService;
import com.truonglq.transaction.service.account.AccountServiceImpl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class AccountServiceImplIntegrationTest {

    @MockitoBean
    private AccountRepository accountRepository;

    @MockitoBean
    private EntityManager entityManager;

    @TestConfiguration
    static class AccountServiceImplTestContextConfiguration {
        @Bean
        public AccountService accountService(AccountRepository accountRepository, EntityManager entityManager) {
            return new AccountServiceImpl(accountRepository, entityManager);
        }
    }

    @Autowired
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAccount() {
        AccountRegistrationRequest request = new AccountRegistrationRequest();
        request.setName("test00");

        Account account = Account.builder()
                .accountNumber("123456789")
                .name(request.getName())
                .balance(BigDecimal.ZERO)
                .status(AccountStatus.ACTIVE)
                .build();

        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountRegistrationResponse response = accountService.createAccount(request);

        assertNotNull(response);
        assertEquals("123456789", response.getAccountNumber());
        assertEquals("test00", response.getName());

        verify(accountRepository, times(1)).save(any(Account.class));
    }

}
