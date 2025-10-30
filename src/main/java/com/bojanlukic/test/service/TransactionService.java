package com.bojanlukic.test.service;

import com.bojanlukic.test.entity.Transaction;
import com.bojanlukic.test.entity.TransactionStatus;
import com.bojanlukic.test.repository.TransactionRepository;
import com.bojanlukic.test.service.exception.TransactionNotExecutedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionService {

    private final AccountService accountService;
    private final TransactionAuditService transactionAuditService;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountService accountService, TransactionAuditService transactionAuditService, TransactionRepository transactionRepository) {
        this.accountService = accountService;
        this.transactionAuditService = transactionAuditService;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Retrieves all transactions from the database.
     */
    public List<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }

    /**
     * Retrieves all transactions associated with a given account,
     * either as sender or recipient.
     */
    public List<Transaction> getTransactionsByAccount(Long accountId) {
        return transactionRepository.findBySenderAccountIdOrRecipientAccountId(accountId, accountId);
    }

    /**
     * Perform the transaction of amount from sender to the recipient account
     * If any error or exception happens during the process, the transaction and changes are rolled back
     */
    @Transactional
    public void executeTransaction(Long senderAccountId, Long recipientAccountId, Long amount) {
        try {
            var payerAccount = accountService.getAccountById(senderAccountId);
            var receiverAccount = accountService.getAccountById(recipientAccountId);
            accountService.withdrawAmountFromAccount(payerAccount, amount);
            accountService.addMoneyToAccount(receiverAccount, amount);

            transactionAuditService.recordTransaction(senderAccountId, recipientAccountId, amount,
                    TransactionStatus.SUCCESS, "");
        } catch (Exception e) {
            transactionAuditService.recordTransaction(senderAccountId, recipientAccountId, amount,
                    TransactionStatus.FAILED, e.getMessage());
            throw new TransactionNotExecutedException("Transaction could not be completed. " + e.getMessage());
        }
    }
}