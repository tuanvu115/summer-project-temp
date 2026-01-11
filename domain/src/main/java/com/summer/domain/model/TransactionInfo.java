package com.summer.domain.model;

import com.summer.domain.enums.PaymentChannel;
import com.summer.domain.enums.TransactionStatus;
import com.summer.domain.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain entity representing a financial transaction.
 * Pure business logic, no framework dependencies.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TransactionInfo {

    private UUID id;
    private TransactionType type;
    private BigDecimal amount;
    private String currency;
    private LocalDate trxDate;
    private String relationNo;
    private AccountInfo fromAccount;
    private AccountInfo toAccount;
    private String channel;
    private PaymentChannel paymentChannel;
    private ReferenceInfo reference;
    private String description;
    private TransactionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private Map<String, Object> metadata;
}
