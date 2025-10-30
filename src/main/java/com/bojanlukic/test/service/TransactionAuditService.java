package com.bojanlukic.test.service;

import com.bojanlukic.test.entity.Transaction;
import com.bojanlukic.test.entity.TransactionStatus;
import com.bojanlukic.test.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransactionAuditService {

    private final TransactionRepository transactionRepository;

    public TransactionAuditService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Records a transaction in the database with the specified status.
     * If the transaction succeeds, it is saved with SUCCESS status.
     * If the transaction fails, it is saved with FAILED status and the provided error message.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordTransaction(Long senderAccountId, Long recipientAccountId, Long amount,
                                  TransactionStatus status, String errorMessage) {
        transactionRepository.save(
                Transaction.builder()
                        .senderAccountId(senderAccountId)
                        .recipientAccountId(recipientAccountId)
                        .amount(amount)
                        .status(status)
                        .description(status == TransactionStatus.SUCCESS
                                ? "Transaction completed successfully."
                                : "Transaction failed. " + errorMessage)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}