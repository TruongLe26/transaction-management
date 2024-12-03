package com.truonglq.transaction.account;

import com.truonglq.transaction.TransactionApplication;
import com.truonglq.transaction.model.entities.Account;
import com.truonglq.transaction.model.enums.AccountStatus;
import com.truonglq.transaction.repository.account.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AccountRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        Account account = Account.builder()
                .accountNumber("123456789")
                .name("test")
                .balance(BigDecimal.ZERO)
                .status(AccountStatus.ACTIVE)
                .build();

        entityManager.persist(account);
        entityManager.flush();
//        accountRepository.save(account);
    }

    @Test
    void whenFindByAccountNumber_thenReturnAccount() {
        Account found = accountRepository.findByAccountNumber("123456789");

        assertThat(found.getName()).isEqualTo("test");
    }

    @Test
    void testFindByAccountNumberNotFound() {
        Account account = accountRepository.findByAccountNumber("999999999");

        assertNull(account);
    }

}
