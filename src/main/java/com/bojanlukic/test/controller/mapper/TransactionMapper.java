package com.bojanlukic.test.controller.mapper;

import com.bojanlukic.test.controller.dto.TransactionDto;
import com.bojanlukic.test.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionDto toDto(Transaction transaction) {
        var dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setSenderAccountId(transaction.getSenderAccountId());
        dto.setRecipientAccountId(transaction.getRecipientAccountId());
        dto.setAmount(transaction.getAmount());
        dto.setStatus(transaction.getStatus());
        dto.setDescription(transaction.getDescription());
        dto.setTimestamp(transaction.getTimestamp());

        return dto;
    }
}