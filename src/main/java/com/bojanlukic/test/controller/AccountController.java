package com.bojanlukic.test.controller;

import com.bojanlukic.test.controller.dto.ApiResponse;
import com.bojanlukic.test.controller.mapper.AccountMapper;
import com.bojanlukic.test.service.AccountService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    public AccountController(AccountService accountService, AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    /**
     * Retrieves account details by account ID.
     */
    @GetMapping("{id}")
    public ResponseEntity<?> getAccountById(@PathVariable @NotNull Long id) {
        var accountDto = accountMapper.toDto(accountService.getAccountById(id));

        return ResponseEntity.ok(new ApiResponse("Account details.", accountDto));
    }

    /**
     * Retrieves account details by account ID.
     */
    @PostMapping("{id}/add")
    public ResponseEntity<?> addMoneyToAccount(@PathVariable @NotNull Long id, @RequestParam @Min(value = 1, message = "Amount must be greater than zero.") Long amount) {
        var account = accountService.getAccountById(id);
        accountService.addMoneyToAccount(account, amount);
        var accountDto = accountMapper.toDto(account);

        return ResponseEntity.ok(
                new ApiResponse("Amount successfully added to the account.", accountDto)
        );
    }

    /**
     * Withdraws money from a specified account.
     */
    @PostMapping("{id}/withdraw")
    public ResponseEntity<?> withdrawMoneyFromAccount(@PathVariable @NotNull Long id, @RequestParam @Min(value = 1, message = "Amount must be greater than zero.") Long amount) {
        var account = accountService.getAccountById(id);
        accountService.withdrawAmountFromAccount(account, amount);
        var accountDto = accountMapper.toDto(account);

        return ResponseEntity.ok(new ApiResponse("Withdrawal completed successfully.", accountDto));
    }
}