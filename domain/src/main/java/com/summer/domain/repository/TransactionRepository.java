package com.summer.domain.repository;

import com.summer.domain.model.TransactionInfo;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface (port) for Transaction aggregate.
 * Implementation will be in infrastructure layer.
 */
public interface TransactionRepository {
    
    TransactionInfo save(TransactionInfo transactionInfo);
    
    Optional<TransactionInfo> findById(UUID id);
}
