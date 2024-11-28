package com.truonglq.transaction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = AccountNotFoundException.class)
    public ResponseEntity<?> accountNotFoundException(AccountNotFoundException accountNotFoundException) {
        return new ResponseEntity<>(accountNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InsufficientBalanceException.class)
    public ResponseEntity<?> insufficientBalanceException(InsufficientBalanceException insufficientBalanceException) {
        return new ResponseEntity<>(insufficientBalanceException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidAmountException.class)
    public ResponseEntity<?> invalidAmountException(InvalidAmountException invalidAmountException) {
        return new ResponseEntity<>(invalidAmountException.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
