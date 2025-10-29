package com.bojanlukic.test.service.exception;

public class WithdrawalNotSupportedException extends RuntimeException{

    public WithdrawalNotSupportedException(String message) {
        super(message);
    }
}