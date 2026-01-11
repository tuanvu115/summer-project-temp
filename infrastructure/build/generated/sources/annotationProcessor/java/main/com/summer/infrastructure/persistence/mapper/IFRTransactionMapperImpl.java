package com.summer.infrastructure.persistence.mapper;

import com.summer.domain.model.AccountInfo;
import com.summer.domain.model.ReferenceInfo;
import com.summer.domain.model.TransactionInfo;
import com.summer.infrastructure.persistence.entity.TransactionEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-11T20:39:07+0700",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.2.1.jar, environment: Java 21 (Eclipse Adoptium)"
)
@Component
public class IFRTransactionMapperImpl implements IFRTransactionMapper {

    @Override
    public TransactionEntity toEntity(TransactionInfo model) {
        if ( model == null ) {
            return null;
        }

        TransactionEntity.TransactionEntityBuilder transactionEntity = TransactionEntity.builder();

        transactionEntity.fromAccountId( modelFromAccountAccountId( model ) );
        transactionEntity.fromAccountName( modelFromAccountAccountName( model ) );
        transactionEntity.toAccountId( modelToAccountAccountId( model ) );
        transactionEntity.toAccountName( modelToAccountAccountName( model ) );
        transactionEntity.clientReferenceId( modelReferenceClientReferenceId( model ) );
        transactionEntity.externalReferenceId( modelReferenceExternalReferenceId( model ) );
        transactionEntity.id( model.getId() );
        transactionEntity.amount( model.getAmount() );
        transactionEntity.currency( model.getCurrency() );
        transactionEntity.trxDate( model.getTrxDate() );
        transactionEntity.relationNo( model.getRelationNo() );
        transactionEntity.type( model.getType() );
        transactionEntity.channel( model.getChannel() );
        transactionEntity.paymentChannel( model.getPaymentChannel() );
        transactionEntity.description( model.getDescription() );
        transactionEntity.status( model.getStatus() );
        transactionEntity.createdAt( model.getCreatedAt() );
        transactionEntity.processedAt( model.getProcessedAt() );

        transactionEntity.fromAccountType( model.getFromAccount().getAccountType().name() );
        transactionEntity.toAccountType( model.getToAccount().getAccountType().name() );

        return transactionEntity.build();
    }

    @Override
    public TransactionInfo toDomain(TransactionEntity entity) {
        if ( entity == null ) {
            return null;
        }

        TransactionInfo.TransactionInfoBuilder transactionInfo = TransactionInfo.builder();

        transactionInfo.id( entity.getId() );
        transactionInfo.type( entity.getType() );
        transactionInfo.amount( entity.getAmount() );
        transactionInfo.currency( entity.getCurrency() );
        transactionInfo.trxDate( entity.getTrxDate() );
        transactionInfo.relationNo( entity.getRelationNo() );
        transactionInfo.channel( entity.getChannel() );
        transactionInfo.paymentChannel( entity.getPaymentChannel() );
        transactionInfo.description( entity.getDescription() );
        transactionInfo.status( entity.getStatus() );
        transactionInfo.createdAt( entity.getCreatedAt() );
        transactionInfo.processedAt( entity.getProcessedAt() );

        transactionInfo.fromAccount( mapFromAccountModel(entity) );
        transactionInfo.toAccount( mapToAccountModel(entity) );
        transactionInfo.reference( mapReferenceModel(entity) );
        transactionInfo.metadata( java.util.Collections.emptyMap() );

        return transactionInfo.build();
    }

    private String modelFromAccountAccountId(TransactionInfo transactionInfo) {
        if ( transactionInfo == null ) {
            return null;
        }
        AccountInfo fromAccount = transactionInfo.getFromAccount();
        if ( fromAccount == null ) {
            return null;
        }
        String accountId = fromAccount.getAccountId();
        if ( accountId == null ) {
            return null;
        }
        return accountId;
    }

    private String modelFromAccountAccountName(TransactionInfo transactionInfo) {
        if ( transactionInfo == null ) {
            return null;
        }
        AccountInfo fromAccount = transactionInfo.getFromAccount();
        if ( fromAccount == null ) {
            return null;
        }
        String accountName = fromAccount.getAccountName();
        if ( accountName == null ) {
            return null;
        }
        return accountName;
    }

    private String modelToAccountAccountId(TransactionInfo transactionInfo) {
        if ( transactionInfo == null ) {
            return null;
        }
        AccountInfo toAccount = transactionInfo.getToAccount();
        if ( toAccount == null ) {
            return null;
        }
        String accountId = toAccount.getAccountId();
        if ( accountId == null ) {
            return null;
        }
        return accountId;
    }

    private String modelToAccountAccountName(TransactionInfo transactionInfo) {
        if ( transactionInfo == null ) {
            return null;
        }
        AccountInfo toAccount = transactionInfo.getToAccount();
        if ( toAccount == null ) {
            return null;
        }
        String accountName = toAccount.getAccountName();
        if ( accountName == null ) {
            return null;
        }
        return accountName;
    }

    private String modelReferenceClientReferenceId(TransactionInfo transactionInfo) {
        if ( transactionInfo == null ) {
            return null;
        }
        ReferenceInfo reference = transactionInfo.getReference();
        if ( reference == null ) {
            return null;
        }
        String clientReferenceId = reference.getClientReferenceId();
        if ( clientReferenceId == null ) {
            return null;
        }
        return clientReferenceId;
    }

    private String modelReferenceExternalReferenceId(TransactionInfo transactionInfo) {
        if ( transactionInfo == null ) {
            return null;
        }
        ReferenceInfo reference = transactionInfo.getReference();
        if ( reference == null ) {
            return null;
        }
        String externalReferenceId = reference.getExternalReferenceId();
        if ( externalReferenceId == null ) {
            return null;
        }
        return externalReferenceId;
    }
}
