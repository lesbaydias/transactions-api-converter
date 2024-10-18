package com.example.transactionservice.mapper;

import com.example.transactionservice.dto.TransactionRequest;
import com.example.transactionservice.dto.TransactionResponse;
import com.example.transactionservice.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(source = "accountFrom", target = "accountFrom")
    @Mapping(source = "accountTo", target = "accountTo")
    @Mapping(source = "currencyShortName", target = "currencyShortName")
    @Mapping(source = "sum", target = "sum")
    @Mapping(source = "expenseCategoryType", target = "expenseCategoryType")
    Transaction toEntity(TransactionRequest request);
    TransactionResponse toResponse(Transaction transaction);
}
