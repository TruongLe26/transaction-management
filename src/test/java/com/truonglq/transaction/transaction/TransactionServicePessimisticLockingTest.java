package com.truonglq.transaction.transaction;

import com.truonglq.transaction.model.entities.Account;
import com.truonglq.transaction.model.enums.AccountStatus;
import com.truonglq.transaction.repository.account.AccountRepository;
import com.truonglq.transaction.service.account.AccountService;
import com.truonglq.transaction.service.account.CustomizedAccountRepositoryContext;
import com.truonglq.transaction.service.transaction.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test-mysql")
@Slf4j
public class TransactionServicePessimisticLockingTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    @MockitoSpyBean
    private AccountService accountService;

    @MockitoSpyBean
    private CustomizedAccountRepositoryContext customizedAccountRepositoryContext;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
    }

    @Test
    void shouldIncrementAccountBalance_withoutConcurrency() throws Exception {
        assertIncrementAccountBalanceWithPessimisticLocking(false, false, 2);
    }

    @Test
    void shouldIncrementAccountBalance_withinPessimisticLockingConcurrencyWithMinimalLockTimeout() throws Exception {
        assertIncrementAccountBalanceWithPessimisticLocking(true, true, 3);
    }

    @Test
    void shouldIncrementAccountBalance_withinPessimisticLockingConcurrencyWithDefaultLockTimeout() throws Exception {
        assertIncrementAccountBalanceWithPessimisticLocking(true, false, 2);
    }

    private void assertIncrementAccountBalanceWithPessimisticLocking(
            boolean simulatePessimisticLocking,
            boolean hasToSetMinimalLockTimeout,
            int expectedNumberOfAccountServiceInvocations) throws Exception {

        if (hasToSetMinimalLockTimeout) {
            long lockTimeoutInMs = customizedAccountRepositoryContext.getMinimalPossibleLockTimeOutInMs();
            when(customizedAccountRepositoryContext.getLockTimeOutInMsForQueryGetAccount()).thenReturn(lockTimeoutInMs);
        }

        if (hasToSetMinimalLockTimeout && customizedAccountRepositoryContext.isRequiredToSetLockTimeoutForTestsAtStartup()) {
            log.info("... set lockTimeOut {} ms through native query at startup ...", customizedAccountRepositoryContext.getMinimalPossibleLockTimeOutInMs());
            TransactionStatus tx = transactionManager.getTransaction(new DefaultTransactionDefinition());

            accountRepository.setLockTimeout(customizedAccountRepositoryContext.getMinimalPossibleLockTimeOutInMs());
            transactionManager.commit(tx);
        }

        if (simulatePessimisticLocking) {
            insertDelayAtTheEndOfPessimisticLockingSection();
        }

//        final Account srcAccount = accountRepository.save(new Account());
        final Account srcAccount = Account.builder()
//                .id("test-0")
                .accountNumber("TEST0")
                .balance(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .name("Test account 0")
                .status(AccountStatus.ACTIVE)
                .updatedAt(LocalDateTime.now())
//                .version(0L)
                .build();
        accountRepository.save(srcAccount);

        final List<BigDecimal> accountAmounts = Arrays.asList(new BigDecimal(10), new BigDecimal(5));

        if (simulatePessimisticLocking) {
            final ExecutorService executor = Executors.newFixedThreadPool(accountAmounts.size());

            for (final BigDecimal amount : accountAmounts) {
                executor.execute(() -> transactionService.addAmount(srcAccount.getId(), amount));
            }

            executor.shutdown();
            assertTrue(executor.awaitTermination(1, TimeUnit.MINUTES));
        } else {
            for (final BigDecimal amount : accountAmounts) {
                transactionService.addAmount(srcAccount.getId(), amount);
            }
        }

        final Account account = accountRepository.findById(srcAccount.getId()).orElseThrow(() -> new IllegalArgumentException("Account not found!"));
        assertAll(
                () -> assertEquals(new BigDecimal("15.00"), account.getBalance()),
                () -> verify(accountService, times(expectedNumberOfAccountServiceInvocations)).addAmount(anyString(), any())
        );
    }

    private void insertDelayAtTheEndOfPessimisticLockingSection() {
        long delay = customizedAccountRepositoryContext.getDelayAtTheEndOfTheQueryForPessimisticLockingTestingInMs();
        doAnswer(invocation -> {
            try {
                TimeUnit.MILLISECONDS.sleep(delay);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            return null;
        }).when(customizedAccountRepositoryContext).insertArtificialDealyAtTheEndOfTheQueryForTestsOnly();
    }

    @Test
    void shouldSetAndGetLockTimeOut() {
        if (customizedAccountRepositoryContext.isRequiredToSetLockTimeoutQueryHint()) {
            assertThrows(UnsupportedOperationException.class, () -> accountRepository.setLockTimeout(0));
            assertThrows(UnsupportedOperationException.class, () -> accountRepository.getLockTimeout());
            return;
        }

        assertSetLockTimeOut(customizedAccountRepositoryContext.getMinimalPossibleLockTimeOutInMs());
        assertSetLockTimeOut(TimeUnit.SECONDS.toMillis(2));
        assertSetLockTimeOut(TimeUnit.MINUTES.toMillis(2));
        assertSetLockTimeOut(TimeUnit.HOURS.toMillis(2));
        assertSetLockTimeOut(TimeUnit.DAYS.toMillis(2));
    }

    private void assertSetLockTimeOut(long expectedMilliseconds) {
        TransactionStatus tx = transactionManager.getTransaction(new DefaultTransactionDefinition());
        accountRepository.setLockTimeout(expectedMilliseconds);
        assertEquals(expectedMilliseconds, accountRepository.getLockTimeout());
        transactionManager.commit(tx);
    }

}
