package com.summer.infrastructure.persistence.repository;

import com.summer.domain.enums.TransactionStatus;
import com.summer.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA Repository for TransactionEntity.
 */
@Repository
public interface TransactionJPA extends JpaRepository<TransactionEntity, UUID> {

    // Custom query method
    List<TransactionEntity> findByCurrency(String currency);

    // Custom query with JPQL
    @Query("SELECT t FROM TransactionEntity t WHERE t.amount > :amount")
    List<TransactionEntity> findByAmountGreaterThan(@Param("amount") BigDecimal amount);

    // Count by status
    long countByStatus(TransactionStatus status);
}
