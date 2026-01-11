package com.summer.presentation.dto;

import com.summer.domain.enums.PaymentChannel;
import com.summer.domain.enums.TransactionStatus;
import com.summer.domain.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record TransactionInfoDTO(
        UUID id,
        TransactionType type,
        BigDecimal amount,
        String currency,
        LocalDate trxDate,
        String relationNo,
        AccountInfoDTO fromAccount,
        AccountInfoDTO toAccount,
        String channel,
        PaymentChannel paymentChannel,
        ReferenceInfoDTO reference,
        String description,
        TransactionStatus status,
        LocalDateTime createdAt,
        LocalDateTime processedAt,
        Map<String, Object> metadata
) {
}
