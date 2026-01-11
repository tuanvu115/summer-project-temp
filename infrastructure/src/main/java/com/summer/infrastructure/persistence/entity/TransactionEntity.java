package com.summer.infrastructure.persistence.entity;

import com.summer.domain.enums.PaymentChannel;
import com.summer.domain.enums.TransactionStatus;
import com.summer.domain.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity for Transaction.
 * Infrastructure concern - separate from domain entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "trx_date", nullable = false)
    private LocalDate trxDate;

    @Column(name = "relation_no", length = 20)
    private String relationNo;

    @Column(name = "type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "from_account_id", nullable = false)
    private String fromAccountId;

    @Column(name = "from_account_type", nullable = false)
    private String fromAccountType;

    @Column(name = "from_account_name", nullable = false)
    private String fromAccountName;

    @Column(name = "to_account_id", nullable = false)
    private String toAccountId;

    @Column(name = "to_account_type", nullable = false)
    private String toAccountType;

    @Column(name = "to_account_name", nullable = false)
    private String toAccountName;

    @Column(name = "channel", length = 50)
    private String channel;

    @Column(name = "payment_channel", length = 20)
    @Enumerated(EnumType.STRING)
    private PaymentChannel paymentChannel;

    @Column(name = "client_reference_id", length = 100)
    private String clientReferenceId;

    @Column(name = "external_reference_id", length = 100)
    private String externalReferenceId;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;


}
