package com.truonglq.transaction.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.truonglq.transaction.TransactionApplication;
import com.truonglq.transaction.dto.requests.AccountRegistrationRequest;
import com.truonglq.transaction.model.entities.Account;
import com.truonglq.transaction.model.enums.AccountStatus;
import com.truonglq.transaction.repository.account.AccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = TransactionApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.yaml"
)
public class AccountRegistrationRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    public void resetDb() {
        accountRepository.deleteAll();
    }

    @Test
    void shouldRegisterAccountSuccessfully() throws Exception {
        AccountRegistrationRequest request = new AccountRegistrationRequest();
        request.setName("test");

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/accounts/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.account_number").isNotEmpty());

        Account registeredAccount = accountRepository.findAll().stream()
                .filter(account -> account.getName().equals("test"))
                .findFirst()
                .orElse(null);

        assertThat(registeredAccount).isNotNull();
        assertThat(registeredAccount.getName()).isEqualTo("test");
        assertThat(registeredAccount.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(registeredAccount.getStatus()).isEqualTo(AccountStatus.ACTIVE);
    }

}
