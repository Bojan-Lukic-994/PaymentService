package com.bojanlukic.test.controller.mapper;

import com.bojanlukic.test.controller.dto.AccountDto;
import com.bojanlukic.test.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountDto toDto(Account account) {
        var dto = new AccountDto();
        dto.setId(account.getId());
        dto.setOwner(account.getOwner());
        dto.setAmount(account.getAmount());

        return dto;
    }
}