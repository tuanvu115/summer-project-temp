package com.summer.application.usecase;

import com.summer.domain.enums.AccountType;
import com.summer.domain.enums.PaymentChannel;
import com.summer.domain.enums.TransactionStatus;
import com.summer.domain.enums.TransactionType;
import com.summer.domain.event.TransactionEventPublisher;
import com.summer.domain.model.AccountInfo;
import com.summer.domain.model.ReferenceInfo;
import com.summer.domain.model.TransactionInfo;
import com.summer.domain.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * APPLICATION LAYER TEST EXAMPLE
 *
 * Unit tests with Mockito - NO Spring context needed.
 * Test use case logic and interactions with dependencies.
 * Fast tests that verify orchestration logic.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Create Transaction Use Case Tests")
class CreateTransactionInfoUseCaseTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionEventPublisher transactionEventPublisher;


    @InjectMocks
    private CreateTransactionUseCase createTransactionUseCase;


    @BeforeEach
    void setUp() {
        // MockitoExtension handles initialization
    }

    @Test
    @DisplayName("Should create transaction successfully")
    void shouldCreateTransactionSuccessfully() {
        // Given
        UUID transactionId = UUID.randomUUID();
        TransactionInfo inputDomain = TransactionInfo.builder()
                .id(transactionId)
                .amount(new BigDecimal("100.50"))
                .currency("USD")
                .trxDate(LocalDate.now())
                .relationNo("000000001")
                .type(TransactionType.TRANSFER)
                .fromAccount(new AccountInfo("ACC001", "John Doe", AccountType.CUSTOMER))
                .toAccount(new AccountInfo("ACC002", "Jane Smith", AccountType.CUSTOMER))
                .channel("MOBILE")
                .paymentChannel(PaymentChannel.CITAD)
                .reference(new ReferenceInfo("CLI-123", "EXT-456"))
                .description("Transfer to Jane")
                .metadata(Collections.singletonMap("note", "Urgent"))
                .build();

        TransactionInfo savedDomain = TransactionInfo.builder()
                .id(transactionId)
                .amount(new BigDecimal("100.50"))
                .currency("USD")
                .trxDate(LocalDate.now())
                .relationNo("000000001")
                .type(TransactionType.TRANSFER)
                .fromAccount(new AccountInfo("ACC001", "John Doe", AccountType.CUSTOMER))
                .toAccount(new AccountInfo("ACC002", "Jane Smith", AccountType.CUSTOMER))
                .channel("MOBILE")
                .paymentChannel(PaymentChannel.CITAD)
                .reference(new ReferenceInfo("CLI-123", "EXT-456"))
                .description("Transfer to Jane")
                .status(TransactionStatus.PENDING)
                .metadata(Collections.singletonMap("note", "Urgent"))
                .build();

        when(transactionRepository.save(any(TransactionInfo.class))).thenReturn(savedDomain);

        // When
        TransactionInfo result = createTransactionUseCase.execute(inputDomain);

        // Then
        assertNotNull(result);
        assertEquals(transactionId, result.getId());
        assertEquals(new BigDecimal("100.50"), result.getAmount());
        assertEquals("USD", result.getCurrency());
        assertEquals(TransactionType.TRANSFER, result.getType());
        assertEquals(TransactionStatus.PENDING, result.getStatus());
        assertEquals("000000001", result.getRelationNo());

        // Verify repository interaction
        verify(transactionRepository, times(1)).save(any(TransactionInfo.class));
    }

    @Test
    @DisplayName("Should create transaction with null trxDate - should set to current date")
    void shouldCreateTransactionWithNullTrxDate() {
        // Given
        TransactionInfo inputDomain = TransactionInfo.builder()
                .id(UUID.randomUUID())
                .amount(new BigDecimal("50.00"))
                .currency("USD")
                .trxDate(null)  // Null trxDate
                .relationNo("REL001")
                .type(TransactionType.TOPUP)
                .build();

        TransactionInfo savedDomain = TransactionInfo.builder()
                .id(inputDomain.getId())
                .amount(new BigDecimal("50.00"))
                .currency("USD")
                .trxDate(LocalDate.now())
                .relationNo("REL001")
                .type(TransactionType.TOPUP)
                .status(TransactionStatus.PENDING)
                .build();

        when(transactionRepository.save(any(TransactionInfo.class))).thenReturn(savedDomain);
        //when(transactionEventPublisher.publishTransactionCreated(any(TransactionInfo.class))).thenReturn();

        // When
        TransactionInfo result = createTransactionUseCase.execute(inputDomain);

        // Then
        assertNotNull(result);
        assertEquals(LocalDate.now(), result.getTrxDate());
        verify(transactionRepository).save(any(TransactionInfo.class));
    }

    @Test
    @DisplayName("Should create transaction with null metadata - should set to empty map")
    void shouldCreateTransactionWithNullMetadata() {
        // Given
        TransactionInfo inputDomain = TransactionInfo.builder()
                .id(UUID.randomUUID())
                .amount(new BigDecimal("75.25"))
                .currency("EUR")
                .trxDate(LocalDate.now())
                .relationNo("REL002")
                .type(TransactionType.WITHDRAW)
                .metadata(null)  // Null metadata
                .build();

        TransactionInfo savedDomain = TransactionInfo.builder()
                .id(inputDomain.getId())
                .amount(new BigDecimal("75.25"))
                .currency("EUR")
                .trxDate(LocalDate.now())
                .relationNo("REL002")
                .type(TransactionType.WITHDRAW)
                .status(TransactionStatus.PENDING)
                .metadata(Collections.emptyMap())
                .build();

        when(transactionRepository.save(any(TransactionInfo.class))).thenReturn(savedDomain);

        // When
        TransactionInfo result = createTransactionUseCase.execute(inputDomain);

        // Then
        assertNotNull(result);
        assertNotNull(result.getMetadata());
        assertTrue(result.getMetadata().isEmpty());
        verify(transactionRepository).save(any(TransactionInfo.class));
    }

    @Test
    @DisplayName("Should throw exception when domain is null")
    void shouldThrowExceptionWhenDomainIsNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            createTransactionUseCase.execute(null);
        });

        verify(transactionRepository, never()).save(any(TransactionInfo.class));
    }

    @Test
    @DisplayName("Should throw exception when relation number is null")
    void shouldThrowExceptionWhenRelationNoIsNull() {
        // Given
        TransactionInfo domain = TransactionInfo.builder()
                .id(UUID.randomUUID())
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .relationNo(null)  // Null relation number
                .type(TransactionType.TRANSFER)
                .build();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createTransactionUseCase.execute(domain);
        });

        assertEquals("Relation number cannot be null or empty", exception.getMessage());
        verify(transactionRepository, never()).save(any(TransactionInfo.class));
    }

    @Test
    @DisplayName("Should throw exception when relation number is empty")
    void shouldThrowExceptionWhenRelationNoIsEmpty() {
        // Given
        TransactionInfo domain = TransactionInfo.builder()
                .id(UUID.randomUUID())
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .relationNo("   ")  // Empty relation number
                .type(TransactionType.TRANSFER)
                .build();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createTransactionUseCase.execute(domain);
        });

        assertEquals("Relation number cannot be null or empty", exception.getMessage());
        verify(transactionRepository, never()).save(any(TransactionInfo.class));
    }

    @Test
    @DisplayName("Should throw exception when relation number exceeds 20 characters")
    void shouldThrowExceptionWhenRelationNoExceedsMaxLength() {
        // Given
        TransactionInfo domain = TransactionInfo.builder()
                .id(UUID.randomUUID())
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .relationNo("123456789012345678901")  // 21 characters
                .type(TransactionType.TRANSFER)
                .build();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createTransactionUseCase.execute(domain);
        });

        assertEquals("Relation number cannot exceed 20 characters", exception.getMessage());
        verify(transactionRepository, never()).save(any(TransactionInfo.class));
    }

    @Test
    @DisplayName("Should throw exception when transaction type is null")
    void shouldThrowExceptionWhenTransactionTypeIsNull() {
        // Given
        TransactionInfo domain = TransactionInfo.builder()
                .id(UUID.randomUUID())
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .relationNo("REL001")
                .type(null)  // Null transaction type
                .build();

        // When & Then
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            createTransactionUseCase.execute(domain);
        });

        assertEquals("Transaction type cannot be null", exception.getMessage());
        verify(transactionRepository, never()).save(any(TransactionInfo.class));
    }

    @Test
    @DisplayName("Should throw exception when amount is null")
    void shouldThrowExceptionWhenAmountIsNull() {
        // Given
        TransactionInfo domain = TransactionInfo.builder()
                .id(UUID.randomUUID())
                .amount(null)  // Null amount
                .currency("USD")
                .relationNo("REL001")
                .type(TransactionType.TRANSFER)
                .build();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createTransactionUseCase.execute(domain);
        });

        assertEquals("Amount cannot be null", exception.getMessage());
        verify(transactionRepository, never()).save(any(TransactionInfo.class));
    }

    @Test
    @DisplayName("Should throw exception when amount is zero")
    void shouldThrowExceptionWhenAmountIsZero() {
        // Given
        TransactionInfo domain = TransactionInfo.builder()
                .id(UUID.randomUUID())
                .amount(BigDecimal.ZERO)  // Zero amount
                .currency("USD")
                .relationNo("REL001")
                .type(TransactionType.TRANSFER)
                .build();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createTransactionUseCase.execute(domain);
        });

        assertEquals("Amount must be positive", exception.getMessage());
        verify(transactionRepository, never()).save(any(TransactionInfo.class));
    }

    @Test
    @DisplayName("Should throw exception when amount is negative")
    void shouldThrowExceptionWhenAmountIsNegative() {
        // Given
        TransactionInfo domain = TransactionInfo.builder()
                .id(UUID.randomUUID())
                .amount(new BigDecimal("-10.00"))  // Negative amount
                .currency("USD")
                .relationNo("REL001")
                .type(TransactionType.TRANSFER)
                .build();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createTransactionUseCase.execute(domain);
        });

        assertEquals("Amount must be positive", exception.getMessage());
        verify(transactionRepository, never()).save(any(TransactionInfo.class));
    }

    @Test
    @DisplayName("Should throw exception when amount has more than 2 decimal places")
    void shouldThrowExceptionWhenAmountHasMoreThanTwoDecimalPlaces() {
        // Given
        TransactionInfo domain = TransactionInfo.builder()
                .id(UUID.randomUUID())
                .amount(new BigDecimal("100.123"))  // 3 decimal places
                .currency("USD")
                .relationNo("REL001")
                .type(TransactionType.TRANSFER)
                .build();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createTransactionUseCase.execute(domain);
        });

        assertEquals("Amount cannot have more than 2 decimal places", exception.getMessage());
        verify(transactionRepository, never()).save(any(TransactionInfo.class));
    }

    @Test
    @DisplayName("Should throw exception when currency is null")
    void shouldThrowExceptionWhenCurrencyIsNull() {
        // Given
        TransactionInfo domain = TransactionInfo.builder()
                .id(UUID.randomUUID())
                .amount(new BigDecimal("100.00"))
                .currency(null)  // Null currency
                .relationNo("REL001")
                .type(TransactionType.TRANSFER)
                .build();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createTransactionUseCase.execute(domain);
        });

        assertEquals("Currency cannot be null or empty", exception.getMessage());
        verify(transactionRepository, never()).save(any(TransactionInfo.class));
    }

    @Test
    @DisplayName("Should throw exception when currency is empty")
    void shouldThrowExceptionWhenCurrencyIsEmpty() {
        // Given
        TransactionInfo domain = TransactionInfo.builder()
                .id(UUID.randomUUID())
                .amount(new BigDecimal("100.00"))
                .currency("   ")  // Empty currency
                .relationNo("REL001")
                .type(TransactionType.TRANSFER)
                .build();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createTransactionUseCase.execute(domain);
        });

        assertEquals("Currency cannot be null or empty", exception.getMessage());
        verify(transactionRepository, never()).save(any(TransactionInfo.class));
    }

    @Test
    @DisplayName("Should throw exception when currency is not 3 characters")
    void shouldThrowExceptionWhenCurrencyIsNot3Characters() {
        // Given
        TransactionInfo domain = TransactionInfo.builder()
                .id(UUID.randomUUID())
                .amount(new BigDecimal("100.00"))
                .currency("US")  // Only 2 characters
                .relationNo("REL001")
                .type(TransactionType.TRANSFER)
                .build();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            createTransactionUseCase.execute(domain);
        });

        assertEquals("Currency must be 3 characters (ISO 4217)", exception.getMessage());
        verify(transactionRepository, never()).save(any(TransactionInfo.class));
    }

    @Test
    @DisplayName("Should create transaction with valid amount with 2 decimal places")
    void shouldCreateTransactionWithValidAmountWith2DecimalPlaces() {
        // Given
        TransactionInfo inputDomain = TransactionInfo.builder()
                .id(UUID.randomUUID())
                .amount(new BigDecimal("99.99"))  // Valid 2 decimal places
                .currency("EUR")
                .relationNo("REL003")
                .type(TransactionType.PAYMENT)
                .build();

        TransactionInfo savedDomain = TransactionInfo.builder()
                .id(inputDomain.getId())
                .amount(new BigDecimal("99.99"))
                .currency("EUR")
                .relationNo("REL003")
                .type(TransactionType.PAYMENT)
                .status(TransactionStatus.PENDING)
                .build();

        when(transactionRepository.save(any(TransactionInfo.class))).thenReturn(savedDomain);

        // When
        TransactionInfo result = createTransactionUseCase.execute(inputDomain);

        // Then
        assertNotNull(result);
        assertEquals(new BigDecimal("99.99"), result.getAmount());
        verify(transactionRepository).save(any(TransactionInfo.class));
    }

    @Test
    @DisplayName("Should create transaction with relation number at max length (20 characters)")
    void shouldCreateTransactionWithMaxLengthRelationNo() {
        // Given
        String maxLengthRelationNo = "12345678901234567890";  // Exactly 20 characters
        TransactionInfo inputDomain = TransactionInfo.builder()
                .id(UUID.randomUUID())
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .relationNo(maxLengthRelationNo)
                .type(TransactionType.TRANSFER)
                .build();

        TransactionInfo savedDomain = TransactionInfo.builder()
                .id(inputDomain.getId())
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .relationNo(maxLengthRelationNo)
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.PENDING)
                .build();

        when(transactionRepository.save(any(TransactionInfo.class))).thenReturn(savedDomain);

        // When
        TransactionInfo result = createTransactionUseCase.execute(inputDomain);

        // Then
        assertNotNull(result);
        assertEquals(maxLengthRelationNo, result.getRelationNo());
        verify(transactionRepository).save(any(TransactionInfo.class));
    }


}
