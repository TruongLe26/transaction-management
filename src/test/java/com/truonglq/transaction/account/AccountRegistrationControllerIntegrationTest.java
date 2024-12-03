package com.truonglq.transaction.account;

import com.truonglq.transaction.JsonUtil;
import com.truonglq.transaction.controller.AccountRegistrationController;
import com.truonglq.transaction.dto.requests.AccountRegistrationRequest;
import com.truonglq.transaction.dto.responses.AccountRegistrationResponse;
import com.truonglq.transaction.model.entities.Account;
import com.truonglq.transaction.service.account.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = AccountRegistrationController.class, excludeAutoConfiguration = {})
public class AccountRegistrationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    @BeforeEach
    public void setUp() throws Exception {
    }

    @Test
    public void whenPostAccount_thenCreateAccount() throws Exception {
        AccountRegistrationRequest request = new AccountRegistrationRequest();
        request.setName("test01");

        AccountRegistrationResponse createdAccount = AccountRegistrationResponse.builder()
                .name("test01")
                .accountNumber("0123456789")
                .build();

        given(accountService.createAccount(Mockito.any())).willReturn(createdAccount);

        mockMvc.perform(post("/api/v1/accounts/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("test01"))
                .andExpect(jsonPath("$.account_number").value("0123456789"));
        verify(accountService, VerificationModeFactory.times(1)).createAccount(Mockito.any());
        reset(accountService);
    }

}
