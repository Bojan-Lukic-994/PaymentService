package com.bojanlukic.test.repository;

import com.bojanlukic.test.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderAccountIdOrRecipientAccountId(Long accountId, Long accountId1);
}