package com.bojanlukic.test.service;

import com.bojanlukic.test.service.exception.TransactionNotExecutedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    private final AccountService accountService;

    public TransactionService(AccountService accountService) {
        this.accountService = accountService;
    }

    //perform the transaction of amount from payer to the receiver account
    //if any error or exception happens during the process, the transaction and changes are rolled back
    @Transactional
    public void executeTransaction(Long payerAccountId, Long receiverAccountId, Long amount) {
        try {
            var payerAccount = accountService.getAccountById(payerAccountId);
            var receiverAccount = accountService.getAccountById(receiverAccountId);
            this.accountService.withdrawAmountFromAccount(payerAccount, amount);
            this.accountService.addAmountToAccount(receiverAccount, amount);
        } catch (Exception e) {
            throw new TransactionNotExecutedException("Transaction could not be completed. " + e.getMessage());
        }
    }
}