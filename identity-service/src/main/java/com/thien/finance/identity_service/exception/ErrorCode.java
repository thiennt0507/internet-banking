package com.thien.finance.identity_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION( "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY("Uncategorized error", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED("Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED( "You do not have permission", HttpStatus.FORBIDDEN),
    CANNOT_SEND_EMAIL("Cannot send email", HttpStatus.BAD_REQUEST),
    ENTITY_NOT_FOUND("Entity not found in database", HttpStatus.NOT_FOUND)
    ;

    ErrorCode(String message, HttpStatusCode statusCode) {
        this.code = statusCode.value();
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
