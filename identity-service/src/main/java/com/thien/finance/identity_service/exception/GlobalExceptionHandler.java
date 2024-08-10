package com.thien.finance.identity_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.thien.finance.identity_service.dto.ApiResponse;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<?>> handlingRuntimeException(RuntimeException e) {
        ApiResponse<?> apiResponse = new ApiResponse();

        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.status(ErrorCode.UNCATEGORIZED_EXCEPTION.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = InvalidBankingUserException.class)
    ResponseEntity<ApiResponse<?>> handlingInvalidBankingUserException(RuntimeException e) {
        ApiResponse<?> apiResponse = new ApiResponse();

        apiResponse.setCode(ErrorCode.UNAUTHENTICATED.getCode());
        apiResponse.setMessage(ErrorCode.UNAUTHENTICATED.getMessage());

        return ResponseEntity.status(ErrorCode.UNAUTHENTICATED.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    ResponseEntity<ApiResponse<?>> handlingEntityNotFoundException(RuntimeException e) {
        ApiResponse<?> apiResponse = new ApiResponse();

        apiResponse.setCode(ErrorCode.ENTITY_NOT_FOUND.getCode());
        apiResponse.setMessage(ErrorCode.ENTITY_NOT_FOUND.getMessage());

        return ResponseEntity.status(ErrorCode.ENTITY_NOT_FOUND.getStatusCode()).body(apiResponse);
    }
    
    @ExceptionHandler(value = ResponseStatusException.class)
    ResponseEntity<ApiResponse<?>> handlingResponseStatusException(RuntimeException e) {
        ApiResponse<?> apiResponse = new ApiResponse();

        apiResponse.setCode(ErrorCode.UNAUTHENTICATED.getCode());
        apiResponse.setMessage(ErrorCode.UNAUTHENTICATED.getMessage());

        return ResponseEntity.status(ErrorCode.UNAUTHENTICATED.getStatusCode()).body(apiResponse);
    }
}
