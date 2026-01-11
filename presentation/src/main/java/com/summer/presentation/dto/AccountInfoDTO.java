package com.summer.presentation.dto;

import com.summer.domain.enums.AccountType;

public record AccountInfoDTO(
    String accountId,
    String accountName,
    AccountType accountType
) {
}
