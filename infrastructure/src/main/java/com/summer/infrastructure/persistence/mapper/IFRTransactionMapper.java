package com.summer.infrastructure.persistence.mapper;

import com.summer.domain.enums.AccountType;
import com.summer.domain.model.AccountInfo;
import com.summer.domain.model.ReferenceInfo;
import com.summer.domain.model.TransactionInfo;
import com.summer.infrastructure.persistence.entity.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * Mapper to convert between Domain and JPA entities.
 * Infrastructure concern - maps domain objects to persistence layer.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IFRTransactionMapper {


    @Mapping(target = "fromAccountId", source = "fromAccount.accountId")
    @Mapping(target = "fromAccountType", expression = "java(model.getFromAccount().getAccountType().name())")
    @Mapping(target = "fromAccountName", source = "fromAccount.accountName")
    @Mapping(target = "toAccountId", source = "toAccount.accountId")
    @Mapping(target = "toAccountType", expression = "java(model.getToAccount().getAccountType().name())")
    @Mapping(target = "toAccountName", source = "toAccount.accountName")
    @Mapping(target = "clientReferenceId", source = "reference.clientReferenceId")
    @Mapping(target = "externalReferenceId", source = "reference.externalReferenceId")
    TransactionEntity toEntity(TransactionInfo model);


    @Mapping(target = "fromAccount", expression = "java(mapFromAccountModel(entity))")
    @Mapping(target = "toAccount", expression = "java(mapToAccountModel(entity))")
    @Mapping(target = "reference", expression = "java(mapReferenceModel(entity))")
    @Mapping(target = "metadata", expression = "java(java.util.Collections.emptyMap())")
    TransactionInfo toDomain(TransactionEntity entity);


    default AccountInfo mapFromAccountModel(TransactionEntity entity) {
        return new AccountInfo(
                entity.getFromAccountId(),
                entity.getFromAccountName(),
                AccountType.valueOf(entity.getFromAccountType())
        );
    }

    default AccountInfo mapToAccountModel(TransactionEntity entity) {
        return new AccountInfo(
                entity.getToAccountId(),
                entity.getToAccountName(),
                AccountType.valueOf(entity.getToAccountType())
        );
    }

    default ReferenceInfo mapReferenceModel(TransactionEntity entity) {
        return new ReferenceInfo(
                entity.getClientReferenceId(),
                entity.getExternalReferenceId()
        );
    }

}
