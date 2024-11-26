package com.truonglq.transaction.controller;

import com.truonglq.transaction.dto.requests.AccountRegistrationRequest;
import com.truonglq.transaction.dto.responses.AccountRegistrationResponse;
import com.truonglq.transaction.service.account.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountRegistrationController {

    AccountService accountService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    AccountRegistrationResponse register(@RequestBody AccountRegistrationRequest request) {
        return accountService.createAccount(request);
    }

}
