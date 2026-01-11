package com.summer.infrastructure.messaging.mapper;

import com.summer.domain.model.AccountInfo;
import com.summer.domain.model.ReferenceInfo;
import com.summer.domain.model.TransactionInfo;
import com.summer.infrastructure.messaging.dto.TransactionEventDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;



@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionEventMapper {
    
    
    TransactionEventDTO toEventDTO(TransactionInfo model);
    
    
}

