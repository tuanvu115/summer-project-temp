package com.summer.application.usecase;

import com.summer.domain.enums.TransactionStatus;
import com.summer.domain.enums.TransactionType;
import com.summer.domain.event.TransactionEventPublisher;
import com.summer.domain.model.TransactionInfo;
import com.summer.domain.repository.TransactionRepository;
import com.sun.tools.javac.Main;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;

/**
 * Use case for creating a new transaction.
 * Application layer - orchestrates domain logic.
 */
@Component
@RequiredArgsConstructor
public class CreateTransactionUseCase {

    private final TransactionRepository transactionRepository;
    private final TransactionEventPublisher transactionEventPublisher;


    public TransactionInfo execute(TransactionInfo domain) {
        validate(domain);

        // set values
        if(domain.getTrxDate() == null){
            domain.setTrxDate(LocalDate.now());
        }
        if(domain.getMetadata() == null){
            domain.setMetadata(Collections.emptyMap());
        }

        domain.setStatus(TransactionStatus.PENDING);
        domain.setCreatedAt(LocalDateTime.now());

        TransactionInfo domainSaved = transactionRepository.save(domain);

        transactionEventPublisher.publishTransactionCreated(domainSaved);

        return domainSaved;
    }

    private void validate(TransactionInfo domain) {
        Objects.requireNonNull(domain, "TransactionInfoDTO cannot be null");

        // Relation No
        String relationNo = domain.getRelationNo();
        if(relationNo == null || relationNo.trim().isEmpty()){
            throw new IllegalArgumentException("Relation number cannot be null or empty");
        }
        if(relationNo.length() > 20){
            throw new IllegalArgumentException("Relation number cannot exceed 20 characters");
        }

        // Transaction Type
        TransactionType transactionType = domain.getType();
        if(transactionType == null){
            throw new NullPointerException("Transaction type cannot be null");
        }

        // Amount
        BigDecimal amount = domain.getAmount();
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (amount.scale() > 2) {
            throw new IllegalArgumentException("Amount cannot have more than 2 decimal places");
        }

        // Currency
        String currency = domain.getCurrency();
        if (currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
        if (currency.length() != 3) {
            throw new IllegalArgumentException("Currency must be 3 characters (ISO 4217)");
        }

    }


}
