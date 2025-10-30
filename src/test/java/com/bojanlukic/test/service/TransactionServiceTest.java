package com.bojanlukic.test.service;

import com.bojanlukic.test.entity.Account;
import com.bojanlukic.test.entity.TransactionStatus;
import com.bojanlukic.test.repository.TransactionRepository;
import com.bojanlukic.test.service.exception.TransactionNotExecutedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    private AccountService accountService;
    private TransactionService transactionService;
    private TransactionAuditService transactionAuditService;

    @BeforeEach
    void setUp() {
        accountService = mock(AccountService.class);
        transactionAuditService = mock(TransactionAuditService.class);
        TransactionRepository transactionRepository = mock(TransactionRepository.class);
        transactionService = new TransactionService(accountService, transactionAuditService, transactionRepository);
    }

    /**
     * Verifies that executeTransaction() correctly performs the transaction
     * by calling withdraw on sender and add on recipient accounts.
     */
    @Test
    void testExecuteTransaction_Success() {
        Account sender = new Account();
        sender.setId(1L);
        sender.setAmount(500L);

        Account receiver = new Account();
        receiver.setId(2L);
        receiver.setAmount(300L);

        when(accountService.getAccountById(1L)).thenReturn(sender);
        when(accountService.getAccountById(2L)).thenReturn(receiver);

        transactionService.executeTransaction(1L, 2L, 200L);

        verify(accountService).withdrawAmountFromAccount(sender, 200L);
        verify(accountService).addMoneyToAccount(receiver, 200L);
        verify(transactionAuditService).recordTransaction(1L, 2L, 200L, TransactionStatus.SUCCESS, "");

    }

    /**
     * Verifies that executeTransaction() throws TransactionNotExecutedException
     * when an unexpected error occurs during the transfer.
     */
    @Test
    void testExecuteTransaction_Failure_ThrowsTransactionNotExecutedException() {
        when(accountService.getAccountById(1L)).thenThrow(new RuntimeException("Execution error"));

        assertThrows(TransactionNotExecutedException.class, () ->
                transactionService.executeTransaction(1L, 2L, 100L)
        );

        verify(transactionAuditService).recordTransaction(1L, 2L, 100L, TransactionStatus.FAILED, "Execution error");
    }
}