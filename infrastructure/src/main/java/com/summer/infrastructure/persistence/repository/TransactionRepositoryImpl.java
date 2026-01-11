package com.summer.infrastructure.persistence.repository;

import com.summer.domain.event.TransactionEventPublisher;
import com.summer.domain.model.TransactionInfo;
import com.summer.domain.repository.TransactionRepository;
import com.summer.infrastructure.persistence.entity.TransactionEntity;
import com.summer.infrastructure.persistence.mapper.IFRTransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Adapter implementation that bridges domain repository interface
 * with Spring Data JPA repository.
 * 
 * This is the IMPLEMENTATION of the domain interface in infrastructure layer.
 * Uses Hexagonal Architecture (Ports & Adapters) pattern.
 */
@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionJPA transactionJPA;
    private final IFRTransactionMapper mapper;


    @Override
    public TransactionInfo save(TransactionInfo transactionInfo) {
        // Convert domain to JPA entity
        TransactionEntity entity = mapper.toEntity(transactionInfo);
        
        // Save using Spring Data JPA repository
        TransactionEntity saved = transactionJPA.save(entity);

        return mapper.toDomain(saved);
    }

    @Override
    public Optional<TransactionInfo> findById(UUID id) {
        return transactionJPA.findById(id)
            .map(mapper::toDomain);
    }
}
