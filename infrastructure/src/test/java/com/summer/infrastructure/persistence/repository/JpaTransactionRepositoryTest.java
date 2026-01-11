//package com.summer.infrastructure.persistence.repository;
//
//import com.summer.infrastructure.entity.TransactionEntity;
//import com.summer.infrastructure.entity.TransactionEntity.TransactionStatusEntity;
//import com.summer.infrastructure.entity.TransactionEntity.TransactionTypeEntity;
//import com.summer.infrastructure.repository.JpaTransactionRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * INFRASTRUCTURE LAYER TEST EXAMPLE
// *
// * Integration test with real database (H2 in-memory).
// * Tests JPA repository functionality and custom queries.
// * Uses @DataJpaTest for focused persistence layer testing.
// */
//@DataJpaTest
//@DisplayName("JPA Transaction Repository Integration Tests")
//class JpaTransactionRepositoryTest {
//
//    @Autowired
//    private JpaTransactionRepository repository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Test
//    @DisplayName("Should save and retrieve transaction by ID")
//    void shouldSaveAndRetrieveTransaction() {
//        // Given
//        TransactionEntity entity = new TransactionEntity(
//            "TXN001",
//            new BigDecimal("100.50"),
//            "USD",
//            TransactionTypeEntity.DEBIT,
//            TransactionStatusEntity.PENDING,
//            LocalDateTime.now()
//        );
//
//        // When
//        TransactionEntity saved = repository.save(entity);
//        entityManager.flush();
//        entityManager.clear();
//
//        // Then
//        Optional<TransactionEntity> found = repository.findById("TXN001");
//        assertTrue(found.isPresent());
//        assertEquals("TXN001", found.get().getId());
//        assertEquals(0, new BigDecimal("100.50").compareTo(found.get().getAmount()));
//        assertEquals("USD", found.get().getCurrency());
//        assertEquals(TransactionTypeEntity.DEBIT, found.get().getType());
//        assertEquals(TransactionStatusEntity.PENDING, found.get().getStatus());
//    }
//
//    @Test
//    @DisplayName("Should find transactions by currency")
//    void shouldFindTransactionsByCurrency() {
//        // Given
//        saveTransaction("TXN002", "100", "USD", TransactionTypeEntity.DEBIT);
//        saveTransaction("TXN003", "200", "EUR", TransactionTypeEntity.CREDIT);
//        saveTransaction("TXN004", "300", "USD", TransactionTypeEntity.TRANSFER);
//        entityManager.flush();
//        entityManager.clear();
//
//        // When
//        List<TransactionEntity> usdTransactions = repository.findByCurrency("USD");
//        List<TransactionEntity> eurTransactions = repository.findByCurrency("EUR");
//
//        // Then
//        assertEquals(2, usdTransactions.size());
//        assertEquals(1, eurTransactions.size());
//        assertTrue(usdTransactions.stream().allMatch(t -> "USD".equals(t.getCurrency())));
//    }
//
//    @Test
//    @DisplayName("Should find transactions with amount greater than specified value")
//    void shouldFindByAmountGreaterThan() {
//        // Given
//        saveTransaction("TXN005", "50.00", "USD", TransactionTypeEntity.DEBIT);
//        saveTransaction("TXN006", "150.00", "USD", TransactionTypeEntity.CREDIT);
//        saveTransaction("TXN007", "250.00", "USD", TransactionTypeEntity.TRANSFER);
//        entityManager.flush();
//        entityManager.clear();
//
//        // When
//        List<TransactionEntity> results = repository.findByAmountGreaterThan(new BigDecimal("100"));
//
//        // Then
//        assertEquals(2, results.size());
//        assertTrue(results.stream()
//            .allMatch(t -> t.getAmount().compareTo(new BigDecimal("100")) > 0));
//    }
//
//    @Test
//    @DisplayName("Should count transactions by status")
//    void shouldCountByStatus() {
//        // Given
//        saveTransaction("TXN008", "100", "USD", TransactionTypeEntity.DEBIT, TransactionStatusEntity.PENDING);
//        saveTransaction("TXN009", "200", "USD", TransactionTypeEntity.CREDIT, TransactionStatusEntity.PENDING);
//        saveTransaction("TXN010", "300", "USD", TransactionTypeEntity.TRANSFER, TransactionStatusEntity.COMPLETED);
//        entityManager.flush();
//
//        // When
//        long pendingCount = repository.countByStatus(TransactionStatusEntity.PENDING);
//        long completedCount = repository.countByStatus(TransactionStatusEntity.COMPLETED);
//
//        // Then
//        assertEquals(2, pendingCount);
//        assertEquals(1, completedCount);
//    }
//
//    @Test
//    @DisplayName("Should delete transaction by ID")
//    void shouldDeleteTransaction() {
//        // Given
//        TransactionEntity entity = saveTransaction("TXN011", "500", "VND", TransactionTypeEntity.DEBIT);
//        entityManager.flush();
//        assertTrue(repository.findById("TXN011").isPresent());
//
//        // When
//        repository.deleteById("TXN011");
//        entityManager.flush();
//
//        // Then
//        assertFalse(repository.findById("TXN011").isPresent());
//    }
//
//    @Test
//    @DisplayName("Should update transaction")
//    void shouldUpdateTransaction() {
//        // Given
//        TransactionEntity entity = saveTransaction("TXN012", "1000", "EUR", TransactionTypeEntity.CREDIT);
//        entityManager.flush();
//        entityManager.clear();
//
//        // When
//        TransactionEntity found = repository.findById("TXN012").orElseThrow();
//        found.setProcessedAt(LocalDateTime.now());
//        repository.save(found);
//        entityManager.flush();
//        entityManager.clear();
//
//        // Then
//        TransactionEntity updated = repository.findById("TXN012").orElseThrow();
//        assertNotNull(updated.getProcessedAt());
//    }
//
//    @Test
//    @DisplayName("Should return empty optional when transaction not found")
//    void shouldReturnEmptyWhenNotFound() {
//        // When
//        Optional<TransactionEntity> result = repository.findById("NONEXISTENT");
//
//        // Then
//        assertFalse(result.isPresent());
//    }
//
//    @Test
//    @DisplayName("Should handle large amounts with correct precision")
//    void shouldHandleLargeAmountsWithCorrectPrecision() {
//        // Given
//        BigDecimal largeAmount = new BigDecimal("999999999.99");
//        TransactionEntity entity = new TransactionEntity(
//            "TXN013",
//            largeAmount,
//            "USD",
//            TransactionTypeEntity.TRANSFER,
//            TransactionStatusEntity.PENDING,
//            LocalDateTime.now()
//        );
//
//        // When
//        repository.save(entity);
//        entityManager.flush();
//        entityManager.clear();
//
//        // Then
//        TransactionEntity found = repository.findById("TXN013").orElseThrow();
//        assertEquals(0, largeAmount.compareTo(found.getAmount()));
//        assertEquals(2, found.getAmount().scale());
//    }
//
//    // Helper method
//    private TransactionEntity saveTransaction(String id, String amount, String currency,
//                                             TransactionTypeEntity type) {
//        return saveTransaction(id, amount, currency, type, TransactionStatusEntity.PENDING);
//    }
//
//    private TransactionEntity saveTransaction(String id, String amount, String currency,
//                                             TransactionTypeEntity type, TransactionStatusEntity status) {
//        TransactionEntity entity = new TransactionEntity(
//            id,
//            new BigDecimal(amount),
//            currency,
//            type,
//            status,
//            LocalDateTime.now()
//        );
//        return entityManager.persist(entity);
//    }
//}
