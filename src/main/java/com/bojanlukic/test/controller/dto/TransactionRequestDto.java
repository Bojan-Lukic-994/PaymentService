package com.bojanlukic.test.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class TransactionRequestDto {

    @NotNull(message = "Sender account ID is required.")
    private Long senderAccountId;

    @NotNull(message = "Recipient account ID is required.")
    private Long recipientAccountId;

    @NotNull(message = "Transaction amount is required.")
    @Min(value = 1, message = "Transaction amount must be greater than zero.")
    private Long amount;


    public Long getSenderAccountId() {
        return senderAccountId;
    }

    public void setSenderAccountId(Long senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public Long getRecipientAccountId() {
        return recipientAccountId;
    }

    public void setRecipientAccountId(Long recipientAccountId) {
        this.recipientAccountId = recipientAccountId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}