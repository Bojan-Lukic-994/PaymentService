package com.bojanlukic.test.service.exception;

public class TransactionNotExecutedException extends RuntimeException {

    public TransactionNotExecutedException(String message) {
        super(message);
    }
}