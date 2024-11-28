package com.truonglq.transaction;

import com.truonglq.transaction.repository.account.AccountRepository;
import com.truonglq.transaction.service.account.AccountService;
import com.truonglq.transaction.service.account.CustomizedAccountRepositoryContext;
import com.truonglq.transaction.service.transaction.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBootTest
@ActiveProfiles("test")
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

}
