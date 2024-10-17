package com.example.transactionservice.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum TypesOfError {
    LIMIT_MUST_BE_GREATER_THAN_ZERO("Limit must be greater than zero.", HttpStatus.BAD_REQUEST),
    CURRENCY_RATE_NOT_FOUND_EXCEPTION("Failed to get the exchange rate for %s .", HttpStatus.NOT_FOUND),
    TRANSACTION_NOT_FOUND("Transaction not found.", HttpStatus.NOT_FOUND),
    LIMIT_NOT_FOUND("Limit not found.", HttpStatus.NOT_FOUND),
    NOT_FOUND_EXCEEDED_TRANSACTIONS("Exceeded transactions not found.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus status;

    TypesOfError(String errorMessage, HttpStatus status){
        this.message = errorMessage;
        this.status = status;
    }
}
