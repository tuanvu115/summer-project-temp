package com.summer.domain.event;

import com.summer.domain.model.TransactionInfo;

/**
 * Port interface for publishing transaction events.
 *
 * This is a domain-level abstraction (Port in Hexagonal Architecture).
 * The actual implementation (Adapter) will be in the infrastructure layer.
 */
public interface TransactionEventPublisher {

    /**
     * Publishes a transaction created event.
     *
     * @param transactionInfo the transaction that was created
     */
    void publishTransactionCreated(TransactionInfo transactionInfo);
}

