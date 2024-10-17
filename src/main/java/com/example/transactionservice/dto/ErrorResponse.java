package com.example.transactionservice.dto;

import com.example.transactionservice.enums.TypesOfError;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private TypesOfError errorType;
    private String message;

    public ErrorResponse(TypesOfError errorType, String message) {
        this.errorType = errorType;
        this.message = message;
    }
}
