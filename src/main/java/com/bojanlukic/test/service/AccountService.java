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

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Returns account by provided id number
     * If account is not found, exception is thrown
     */
    public Account getAccountById(Long id) {
        var optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) {
            throw new AccountNotFoundException("Account with id number " + id + " not found.");
        }

        return optionalAccount.get();
    }

    /**
     * adds provided amount of money to the provided account
     * If account is not found, exception is thrown
     */
    @Transactional
    public void addMoneyToAccount(Account account, Long addedAmount) {
        account.setAmount(account.getAmount() + addedAmount);
        accountRepository.save(account);
    }

    /**
     * Removes the requested amount of money from the provided account
     * If the requested amount is larger than the current amount, an exception is thrown
     */
    @Transactional
    public void withdrawAmountFromAccount(Account account, Long requestedAmount) {
        int ACCOUNT_BOTTOM_AMOUNT = 0;
        if (account.getAmount() - requestedAmount < ACCOUNT_BOTTOM_AMOUNT) {
            throw new WithdrawalNotSupportedException("Withdrawal failed: The requested amount (" + requestedAmount + ") exceeds available balance (" + account.getAmount() + ") for the account with id number " + account.getId() + ".");
        }

        account.setAmount(account.getAmount() - requestedAmount);
        accountRepository.save(account);
    }
}