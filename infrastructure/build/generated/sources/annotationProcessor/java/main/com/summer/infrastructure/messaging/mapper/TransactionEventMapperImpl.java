package com.summer.infrastructure.messaging.mapper;

import com.summer.domain.model.TransactionInfo;
import com.summer.infrastructure.messaging.dto.TransactionEventDTO;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-11T20:39:07+0700",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.2.1.jar, environment: Java 21 (Eclipse Adoptium)"
)
@Component
public class TransactionEventMapperImpl implements TransactionEventMapper {

    @Override
    public TransactionEventDTO toEventDTO(TransactionInfo model) {
        if ( model == null ) {
            return null;
        }

        TransactionEventDTO.TransactionEventDTOBuilder transactionEventDTO = TransactionEventDTO.builder();

        transactionEventDTO.id( model.getId() );
        transactionEventDTO.type( model.getType() );
        transactionEventDTO.amount( model.getAmount() );
        transactionEventDTO.currency( model.getCurrency() );
        transactionEventDTO.trxDate( model.getTrxDate() );
        transactionEventDTO.relationNo( model.getRelationNo() );
        transactionEventDTO.fromAccount( model.getFromAccount() );
        transactionEventDTO.toAccount( model.getToAccount() );
        transactionEventDTO.channel( model.getChannel() );
        transactionEventDTO.paymentChannel( model.getPaymentChannel() );
        transactionEventDTO.reference( model.getReference() );
        transactionEventDTO.description( model.getDescription() );
        transactionEventDTO.status( model.getStatus() );
        transactionEventDTO.createdAt( model.getCreatedAt() );
        transactionEventDTO.processedAt( model.getProcessedAt() );
        Map<String, Object> map = model.getMetadata();
        if ( map != null ) {
            transactionEventDTO.metadata( new LinkedHashMap<String, Object>( map ) );
        }

        return transactionEventDTO.build();
    }
}
