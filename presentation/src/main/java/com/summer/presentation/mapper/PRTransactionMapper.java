package com.summer.presentation.mapper;

import com.summer.domain.model.AccountInfo;
import com.summer.domain.model.ReferenceInfo;
import com.summer.domain.model.TransactionInfo;
import com.summer.presentation.dto.AccountInfoDTO;
import com.summer.presentation.dto.ReferenceInfoDTO;
import com.summer.presentation.dto.TransactionInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PRTransactionMapper {
    
    TransactionInfoDTO toDTO(TransactionInfo transaction);
    
    AccountInfoDTO toDTO(AccountInfo account);
    
    ReferenceInfoDTO toDTO(ReferenceInfo reference);

    TransactionInfo toDomain(TransactionInfoDTO transactionInfoDTO);

    AccountInfo toDomain(AccountInfoDTO dto);
    
    ReferenceInfo toDomain(ReferenceInfoDTO dto);
}
