package com.summer.infrastructure.messaging.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.summer.domain.enums.PaymentChannel;
import com.summer.domain.enums.TransactionStatus;
import com.summer.domain.enums.TransactionType;
import com.summer.domain.model.AccountInfo;
import com.summer.domain.model.ReferenceInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * DTO for TransactionInfo Kafka messages.
 *
 * This is a separate DTO to decouple domain model from messaging serialization.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEventDTO {

    private UUID id;
    private TransactionType type;
    private BigDecimal amount;
    private String currency;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate trxDate;

    private String relationNo;
    private AccountInfo fromAccount;
    private AccountInfo toAccount;
    private String channel;
    private PaymentChannel paymentChannel;
    private ReferenceInfo reference;
    private String description;
    private TransactionStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime processedAt;

    private Map<String, Object> metadata;

}

