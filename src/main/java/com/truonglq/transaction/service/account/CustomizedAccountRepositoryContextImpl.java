package com.truonglq.transaction.service.account;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class CustomizedAccountRepositoryContextImpl extends CustomizedAccountRepositoryContext {
    public CustomizedAccountRepositoryContextImpl(EntityManager em) {
        super(em);
    }
}
