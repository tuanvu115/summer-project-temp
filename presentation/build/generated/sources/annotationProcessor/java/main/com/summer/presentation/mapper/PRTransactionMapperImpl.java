package com.summer.presentation.mapper;

import com.summer.domain.enums.AccountType;
import com.summer.domain.enums.PaymentChannel;
import com.summer.domain.enums.TransactionStatus;
import com.summer.domain.enums.TransactionType;
import com.summer.domain.model.AccountInfo;
import com.summer.domain.model.ReferenceInfo;
import com.summer.domain.model.TransactionInfo;
import com.summer.presentation.dto.AccountInfoDTO;
import com.summer.presentation.dto.ReferenceInfoDTO;
import com.summer.presentation.dto.TransactionInfoDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-11T14:36:54+0700",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.2.1.jar, environment: Java 21 (Eclipse Adoptium)"
)
@Component
public class PRTransactionMapperImpl implements PRTransactionMapper {

    @Override
    public TransactionInfoDTO toDTO(TransactionInfo transaction) {
        if ( transaction == null ) {
            return null;
        }

        UUID id = null;
        TransactionType type = null;
        BigDecimal amount = null;
        String currency = null;
        LocalDate trxDate = null;
        String relationNo = null;
        AccountInfoDTO fromAccount = null;
        AccountInfoDTO toAccount = null;
        String channel = null;
        PaymentChannel paymentChannel = null;
        ReferenceInfoDTO reference = null;
        String description = null;
        TransactionStatus status = null;
        LocalDateTime createdAt = null;
        LocalDateTime processedAt = null;
        Map<String, Object> metadata = null;

        id = transaction.getId();
        type = transaction.getType();
        amount = transaction.getAmount();
        currency = transaction.getCurrency();
        trxDate = transaction.getTrxDate();
        relationNo = transaction.getRelationNo();
        fromAccount = toDTO( transaction.getFromAccount() );
        toAccount = toDTO( transaction.getToAccount() );
        channel = transaction.getChannel();
        paymentChannel = transaction.getPaymentChannel();
        reference = toDTO( transaction.getReference() );
        description = transaction.getDescription();
        status = transaction.getStatus();
        createdAt = transaction.getCreatedAt();
        processedAt = transaction.getProcessedAt();
        Map<String, Object> map = transaction.getMetadata();
        if ( map != null ) {
            metadata = new LinkedHashMap<String, Object>( map );
        }

        TransactionInfoDTO transactionInfoDTO = new TransactionInfoDTO( id, type, amount, currency, trxDate, relationNo, fromAccount, toAccount, channel, paymentChannel, reference, description, status, createdAt, processedAt, metadata );

        return transactionInfoDTO;
    }

    @Override
    public AccountInfoDTO toDTO(AccountInfo account) {
        if ( account == null ) {
            return null;
        }

        String accountId = null;
        String accountName = null;
        AccountType accountType = null;

        accountId = account.getAccountId();
        accountName = account.getAccountName();
        accountType = account.getAccountType();

        AccountInfoDTO accountInfoDTO = new AccountInfoDTO( accountId, accountName, accountType );

        return accountInfoDTO;
    }

    @Override
    public ReferenceInfoDTO toDTO(ReferenceInfo reference) {
        if ( reference == null ) {
            return null;
        }

        String clientReferenceId = null;
        String externalReferenceId = null;

        clientReferenceId = reference.getClientReferenceId();
        externalReferenceId = reference.getExternalReferenceId();

        ReferenceInfoDTO referenceInfoDTO = new ReferenceInfoDTO( clientReferenceId, externalReferenceId );

        return referenceInfoDTO;
    }

    @Override
    public TransactionInfo toDomain(TransactionInfoDTO transactionInfoDTO) {
        if ( transactionInfoDTO == null ) {
            return null;
        }

        TransactionInfo.TransactionInfoBuilder transactionInfo = TransactionInfo.builder();

        transactionInfo.id( transactionInfoDTO.id() );
        transactionInfo.type( transactionInfoDTO.type() );
        transactionInfo.amount( transactionInfoDTO.amount() );
        transactionInfo.currency( transactionInfoDTO.currency() );
        transactionInfo.trxDate( transactionInfoDTO.trxDate() );
        transactionInfo.relationNo( transactionInfoDTO.relationNo() );
        transactionInfo.fromAccount( toDomain( transactionInfoDTO.fromAccount() ) );
        transactionInfo.toAccount( toDomain( transactionInfoDTO.toAccount() ) );
        transactionInfo.channel( transactionInfoDTO.channel() );
        transactionInfo.paymentChannel( transactionInfoDTO.paymentChannel() );
        transactionInfo.reference( toDomain( transactionInfoDTO.reference() ) );
        transactionInfo.description( transactionInfoDTO.description() );
        transactionInfo.status( transactionInfoDTO.status() );
        transactionInfo.createdAt( transactionInfoDTO.createdAt() );
        transactionInfo.processedAt( transactionInfoDTO.processedAt() );
        Map<String, Object> map = transactionInfoDTO.metadata();
        if ( map != null ) {
            transactionInfo.metadata( new LinkedHashMap<String, Object>( map ) );
        }

        return transactionInfo.build();
    }

    @Override
    public AccountInfo toDomain(AccountInfoDTO dto) {
        if ( dto == null ) {
            return null;
        }

        AccountInfo.AccountInfoBuilder accountInfo = AccountInfo.builder();

        accountInfo.accountId( dto.accountId() );
        accountInfo.accountName( dto.accountName() );
        accountInfo.accountType( dto.accountType() );

        return accountInfo.build();
    }

    @Override
    public ReferenceInfo toDomain(ReferenceInfoDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ReferenceInfo.ReferenceInfoBuilder referenceInfo = ReferenceInfo.builder();

        referenceInfo.clientReferenceId( dto.clientReferenceId() );
        referenceInfo.externalReferenceId( dto.externalReferenceId() );

        return referenceInfo.build();
    }
}
