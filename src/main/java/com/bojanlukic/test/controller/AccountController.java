package com.bojanlukic.test.controller;

import com.bojanlukic.test.controller.dto.ApiResponse;
import com.bojanlukic.test.controller.dto.TransactionRequestDto;
import com.bojanlukic.test.controller.mapper.AccountMapper;
import com.bojanlukic.test.service.AccountService;
import com.bojanlukic.test.service.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final AccountMapper accountMapper;

    public AccountController(AccountService accountService, TransactionService transactionService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.accountMapper = accountMapper;
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getAccountById(@PathVariable @NotNull Long id) {
        var accountDto = accountMapper.toDto(accountService.getAccountById(id));

        return ResponseEntity.ok(new ApiResponse("Account details.", accountDto));
    }

    @PostMapping("{id}/add")
    public ResponseEntity<?> addMoneyToAccount(@PathVariable @NotNull Long id, @RequestParam @Min(value = 1, message = "Amount must be greater than zero.") Long amount) {
        var account = accountService.getAccountById(id);
        accountService.addAmountToAccount(account, amount);
        var accountDto = accountMapper.toDto(account);

        return ResponseEntity.ok(
                new ApiResponse("Amount successfully added to the account.", accountDto)
        );
    }

    @PostMapping("{id}/withdraw")
    public ResponseEntity<?> withdrawMoneyFromAccount(@PathVariable @NotNull Long id, @RequestParam @Min(value = 1, message = "Amount must be greater than zero.") Long amount) {
        var account = accountService.getAccountById(id);
        accountService.withdrawAmountFromAccount(account, amount);
        var accountDto = accountMapper.toDto(account);

        return ResponseEntity.ok(new ApiResponse("Withdrawal completed successfully.", accountDto));
    }

    @PostMapping("/transaction")
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