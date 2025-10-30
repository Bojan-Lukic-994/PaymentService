package com.bojanlukic.test.controller;

import com.bojanlukic.test.controller.dto.ApiResponse;
import com.bojanlukic.test.controller.dto.TransactionRequestDto;
import com.bojanlukic.test.controller.mapper.AccountMapper;
import com.bojanlukic.test.controller.mapper.TransactionMapper;
import com.bojanlukic.test.service.AccountService;
import com.bojanlukic.test.service.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    public TransactionController(TransactionService transactionService, TransactionMapper transactionMapper, AccountService accountService, AccountMapper accountMapper) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    /**
     * Returns a list of all transactions in the system.
     * This endpoint fetches all transaction records from the database.
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllTransactions() {
        var transactions = transactionService.findAllTransactions().stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse("All transactions retrieved successfully.", transactions));
    }

    /**
     * Returns all transactions related to a specific account.
     * This includes both sent and received transactions.
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<ApiResponse> getTransactionsByAccount(@PathVariable @NotNull Long accountId) {
        var transactions = transactionService.getTransactionsByAccount(accountId).stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse("Transactions for account " + accountId + " retrieved successfully.", transactions));
    }

    /**
     * Executes a money transfer from one account to another.
     * This endpoint performs the transaction, updates account balances,
     * and records the transaction (success or failure) for audit purposes.
     */
    @PostMapping("")
    public ResponseEntity<ApiResponse> executeTransaction(@Valid @RequestBody TransactionRequestDto request) {
        var senderAccountId = request.getSenderAccountId();
        var recipientAccountId = request.getRecipientAccountId();
        var amount = request.getAmount();

        transactionService.executeTransaction(
                senderAccountId,
                recipientAccountId,
                amount
        );

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("sender account", accountMapper.toDto(accountService.getAccountById(senderAccountId)));
        responseData.put("recipient account", accountMapper.toDto(accountService.getAccountById(recipientAccountId)));
        responseData.put("money sent", amount);

        return ResponseEntity.ok(new ApiResponse("Transaction completed successfully.", responseData));
    }
}