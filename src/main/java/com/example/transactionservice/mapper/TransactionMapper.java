package com.example.transactionservice.mapper;

import com.example.transactionservice.dto.TransactionRequest;
import com.example.transactionservice.dto.TransactionResponse;
import com.example.transactionservice.model.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    Transaction toEntity(TransactionRequest request);
    TransactionResponse toResponse(Transaction transaction);
}
