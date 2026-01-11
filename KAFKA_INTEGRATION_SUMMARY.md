# Kafka Integration Summary

## Overview
Implemented Kafka event publishing for TransactionInfo after successful save operations. The implementation follows Hexagonal Architecture (Ports & Adapters) pattern, separating domain logic from infrastructure concerns.

## Changes Made

### 1. Domain Layer (`domain` module)

#### Created: `TransactionEventPublisher.java`
**Location**: `/domain/src/main/java/com/summer/domain/event/TransactionEventPublisher.java`

- **Purpose**: Domain port (interface) for publishing transaction events
- **Method**: `publishTransactionCreated(TransactionInfo transactionInfo)`
- **Pattern**: Port in Hexagonal Architecture - defines what the domain needs without knowing how it's implemented

### 2. Infrastructure Layer (`infrastructure` module)

#### Created: `TransactionEventDTO.java`
**Location**: `/infrastructure/src/main/java/com/summer/infrastructure/messaging/dto/TransactionEventDTO.java`

- **Purpose**: Data Transfer Object for Kafka messages
- **Features**:
  - Separate DTO to decouple domain model from messaging serialization
  - Jackson annotations for proper JSON serialization (`@JsonFormat`)
  - Nested DTOs for AccountDTO and ReferenceDTO
  - All transaction fields mapped appropriately

#### Created: `TransactionEventMapper.java`
**Location**: `/infrastructure/src/main/java/com/summer/infrastructure/messaging/mapper/TransactionEventMapper.java`

- **Purpose**: Maps between domain model (`TransactionInfo`) and messaging DTO (`TransactionEventDTO`)
- **Method**: `toEventDTO(TransactionInfo transactionInfo)`
- **Features**:
  - Handles null-safe conversions
  - Maps nested objects (AccountInfo → AccountDTO, ReferenceInfo → ReferenceDTO)
  - Spring `@Component` for dependency injection

#### Created: `TransactionEventPublisherImpl.java`
**Location**: `/infrastructure/src/main/java/com/summer/infrastructure/messaging/producer/TransactionEventPublisherImpl.java`

- **Purpose**: Kafka adapter implementation of `TransactionEventPublisher` interface
- **Pattern**: Adapter in Hexagonal Architecture - implements the domain port using Kafka
- **Dependencies**:
  - `KafkaTemplate<String, TransactionEventDTO>` - Spring Kafka template
  - `TransactionEventMapper` - for domain to DTO conversion
- **Features**:
  - Uses transaction ID as message key for partitioning
  - Asynchronous publishing with `CompletableFuture`
  - Comprehensive logging (info and error levels)
  - Error handling: doesn't throw exceptions to avoid transaction rollback
  - Configurable topic name via `kafka.topic.transaction-created` property

#### Updated: `TransactionRepositoryImpl.java`
**Location**: `/infrastructure/src/main/java/com/summer/infrastructure/persistence/repository/TransactionRepositoryImpl.java`

**Changes**:
- Added `TransactionEventPublisher` dependency injection
- Modified `save()` method to publish Kafka event after successful save:
  ```java
  TransactionInfo savedTransaction = mapper.toDomain(saved);
  eventPublisher.publishTransactionCreated(savedTransaction);
  return savedTransaction;
  ```

### 3. Configuration (`presentation` module)

#### Updated: `kafka.yml`
**Location**: `/presentation/src/main/resources/kafka.yml`

**Added**:
```yaml
# Kafka Topics Configuration
kafka:
  topic:
    transaction-created: ${KAFKA_TOPIC_TRANSACTION_CREATED:transaction-created}
```

- **Environment Variable**: `KAFKA_TOPIC_TRANSACTION_CREATED`
- **Default Value**: `transaction-created`
- **Usage**: Allows topic name to be configured externally

## Architecture Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                         Domain Layer                             │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │ TransactionEventPublisher (Port/Interface)                │   │
│  │ - publishTransactionCreated(TransactionInfo)              │   │
│  └──────────────────────────────────────────────────────────┘   │
└───────────────────────────────┬─────────────────────────────────┘
                                │ implements
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                      Infrastructure Layer                        │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │ TransactionEventPublisherImpl (Adapter)                   │   │
│  │ - Uses KafkaTemplate                                      │   │
│  │ - Uses TransactionEventMapper                             │   │
│  │ - Publishes to Kafka topic                                │   │
│  └──────────────────────────────────────────────────────────┘   │
│                                                                   │
│  ┌──────────────────────────────────────────────────────────┐   │
│  │ TransactionRepositoryImpl                                 │   │
│  │ save() {                                                  │   │
│  │   1. Save to database                                     │   │
│  │   2. Publish to Kafka ← NEW                              │   │
│  │   3. Return saved entity                                  │   │
│  │ }                                                         │   │
│  └──────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘
```

## Message Format

### Kafka Message Structure
- **Topic**: `transaction-created` (configurable)
- **Key**: Transaction UUID (as String)
- **Value**: TransactionEventDTO (JSON serialized)

### Example JSON Message
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "type": "TRANSFER",
  "amount": 1000.00,
  "currency": "USD",
  "trxDate": "2026-01-04",
  "relationNo": "REL12345",
  "fromAccount": {
    "accountNo": "ACC001",
    "accountName": "John Doe",
    "bankCode": null
  },
  "toAccount": {
    "accountNo": "ACC002",
    "accountName": "Jane Smith",
    "bankCode": null
  },
  "channel": "MOBILE",
  "paymentChannel": "BANK_TRANSFER",
  "reference": {
    "referenceNo": "REF12345",
    "externalRef": "EXT67890"
  },
  "description": "Payment for invoice",
  "status": "COMPLETED",
  "createdAt": "2026-01-04T10:30:00",
  "processedAt": "2026-01-04T10:30:05",
  "metadata": {
    "additionalInfo": "value"
  }
}
```

## Configuration

### Environment Variables
- `KAFKA_BOOTSTRAP_SERVERS`: Kafka broker addresses (default: `localhost:9092`)
- `KAFKA_TOPIC_TRANSACTION_CREATED`: Topic name (default: `transaction-created`)

### Kafka Producer Settings
- **Key Serializer**: StringSerializer
- **Value Serializer**: JsonSerializer
- **ACKs**: all (ensures message durability)
- **Retries**: 3
- **Type Headers**: Disabled (`spring.json.add.type.headers: false`)

## Testing Recommendations

### Unit Tests
1. Test `TransactionEventMapper`:
   - Null handling
   - Complete field mapping
   - Nested object mapping

2. Test `TransactionEventPublisherImpl`:
   - Successful publish
   - Error handling
   - Logging verification

### Integration Tests
1. Test `TransactionRepositoryImpl`:
   - Verify event is published after save
   - Verify database transaction is not rolled back on Kafka failure
   - Verify message key equals transaction ID

2. End-to-End Test:
   - Save transaction via API
   - Verify Kafka consumer receives event
   - Verify message content matches saved transaction

## Key Design Decisions

### 1. Error Handling Strategy
**Decision**: Don't throw exceptions from Kafka publishing
**Rationale**: 
- Event publishing is a non-critical operation
- Database save should succeed even if Kafka is temporarily unavailable
- Errors are logged for monitoring and alerting

### 2. Asynchronous Publishing
**Decision**: Use asynchronous `CompletableFuture` approach
**Rationale**:
- Non-blocking operation
- Better performance
- Callback-based logging

### 3. Separate DTO Layer
**Decision**: Create separate `TransactionEventDTO` instead of serializing domain model
**Rationale**:
- Decouples domain from messaging format
- Allows different JSON structure if needed
- Better control over serialization

### 4. Transaction ID as Message Key
**Decision**: Use transaction UUID as Kafka message key
**Rationale**:
- Ensures messages for same transaction go to same partition
- Enables partition-level ordering
- Useful for Kafka Streams processing

## Dependencies

Already present in `infrastructure/build.gradle`:
```gradle
implementation 'org.springframework.boot:spring-boot-starter-kafka'
implementation 'com.fasterxml.jackson.core:jackson-databind'
```

## Monitoring & Operations

### Logging
- **Info Level**: Successful publishes with partition and offset
- **Error Level**: Failed publishes with transaction ID and exception

### Metrics to Monitor
1. Kafka producer metrics (send rate, error rate)
2. Message lag in consumer groups
3. Failed publish attempts in logs

### Troubleshooting
- Check logs for "Failed to publish transaction event"
- Verify Kafka broker connectivity
- Confirm topic exists and is accessible
- Check serialization errors

## Future Enhancements

1. **Transactional Outbox Pattern**: For guaranteed event delivery
2. **Dead Letter Queue**: For failed messages
3. **Event Versioning**: Add schema version to DTO
4. **Metrics**: Add custom metrics for event publishing
5. **Circuit Breaker**: Add resilience pattern for Kafka failures
6. **Event Types**: Support different event types (created, updated, deleted)

## Files Created/Modified

### Created (4 files):
1. `domain/src/main/java/com/summer/domain/event/TransactionEventPublisher.java`
2. `infrastructure/src/main/java/com/summer/infrastructure/messaging/dto/TransactionEventDTO.java`
3. `infrastructure/src/main/java/com/summer/infrastructure/messaging/mapper/TransactionEventMapper.java`
4. `infrastructure/src/main/java/com/summer/infrastructure/messaging/producer/TransactionEventPublisherImpl.java`

### Modified (2 files):
1. `infrastructure/src/main/java/com/summer/infrastructure/persistence/repository/TransactionRepositoryImpl.java`
2. `presentation/src/main/resources/kafka.yml`

---

**Implementation Date**: January 4, 2026  
**Architecture Pattern**: Hexagonal Architecture (Ports & Adapters)  
**Messaging Platform**: Apache Kafka with Spring Kafka

