package com.summer.application.usecase;

import com.summer.application.exception.TransactionNotFoundException;
import com.summer.domain.model.TransactionInfo;
import com.summer.domain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

/**
 * Use case for retrieving a transaction by ID.
 */
@Component
@RequiredArgsConstructor
public class GetTransactionUseCase {

    private final TransactionRepository transactionRepository;


    public TransactionInfo execute(UUID transactionId) {
        Objects.requireNonNull(transactionId, "Transaction ID cannot be null");
        return transactionRepository
                .findById(transactionId).orElseThrow(() -> new TransactionNotFoundException(transactionId));
    }

}
