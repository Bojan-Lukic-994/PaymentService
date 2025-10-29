package com.bojanlukic.test.service;

import com.bojanlukic.test.entity.Account;
import com.bojanlukic.test.repository.AccountRepository;
import com.bojanlukic.test.service.exception.AccountNotFoundException;
import com.bojanlukic.test.service.exception.WithdrawalNotSupportedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final int ACCOUNT_BOTTOM_AMOUNT = 0;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    //method that returns account by provided id number
    //if account is not found, exception is thrown
    public Account getAccountById(Long id) {
        var optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) {
            throw new AccountNotFoundException("Account with id number " + id + " not found.");
        }

        return optionalAccount.get();
    }

    //adds provided amount to the selected account
    @Transactional
    public void addAmountToAccount(Account account, Long addedAmount) {
        account.setAmount(account.getAmount() + addedAmount);
        accountRepository.save(account);
    }

    //removes the requested amount from the selected account
    //if the requested amount is more than current amount, the exception is thrown
    @Transactional
    public void withdrawAmountFromAccount(Account account, Long requestedAmount) {
        if (account.getAmount() - requestedAmount < ACCOUNT_BOTTOM_AMOUNT) {
            throw new WithdrawalNotSupportedException("Withdrawal failed: The requested amount (" + requestedAmount + ") exceeds available balance (" + account.getAmount() + ") for the account with id number " + account.getId() + ".");
        }

        account.setAmount(account.getAmount() - requestedAmount);
        accountRepository.save(account);
    }
}