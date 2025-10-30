package com.bojanlukic.test.service;

import com.bojanlukic.test.entity.Account;
import com.bojanlukic.test.repository.AccountRepository;
import com.bojanlukic.test.service.exception.AccountNotFoundException;
import com.bojanlukic.test.service.exception.WithdrawalNotSupportedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    private AccountRepository accountRepository;
    private AccountService accountService;
    private Account testAccount;


    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        accountService = new AccountService(accountRepository);
        testAccount = new Account();
        testAccount.setId(1L);
        testAccount.setAmount(1000L);
    }

    /**
     * Verifies that getAccountById() returns an account successfully
     * when the account exists in the repository.
     */
    @Test
    void testGetAccountById_Success() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        Account result = accountService.getAccountById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1000L, result.getAmount());
        verify(accountRepository, times(1)).findById(1L);
    }

    /**
     * Verifies that getAccountById() throws AccountNotFoundException
     * when the requested account does not exist in the repository.
     */
    @Test
    void testGetAccountById_NotFound() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(
                AccountNotFoundException.class,
                () -> accountService.getAccountById(99L)
        );

        assertEquals("Account with id number 99 not found.", exception.getMessage());
        verify(accountRepository, times(1)).findById(99L);
    }

    /**
     * Verifies that addAmountToAccount() correctly increases the balance
     * and saves the updated account in the repository.
     */
    @Test
    void testAddAmountToAccount() {
        Account account = new Account();
        account.setId(1L);
        account.setAmount(100L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.addAmountToAccount(account, 50L);

        assertEquals(150L, account.getAmount());
        verify(accountRepository).save(account);
    }

    /**
     * Verifies that withdrawAmountFromAccount() correctly decreases the balance
     * when the account has enough funds.
     */
    @Test
    void testWithdrawMoneyFromAccount_Success() {
        Account account = new Account();
        account.setId(1L);
        account.setAmount(200L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.withdrawAmountFromAccount(account, 50L);

        assertEquals(150L, account.getAmount());
        verify(accountRepository).save(account);
    }

    /**
     * Verifies that withdrawAmountFromAccount() throws a WithdrawalNotSupportedException
     * when attempting to withdraw more than the current balance.
     */
    @Test
    void testWithdrawMoneyFromAccount_ThrowsException_WhenInsufficientFunds() {
        Account account = new Account();
        account.setId(1L);
        account.setAmount(100L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(WithdrawalNotSupportedException.class, () ->
                accountService.withdrawAmountFromAccount(account, 200L)
        );

        verify(accountRepository, never()).save(account);
    }
}