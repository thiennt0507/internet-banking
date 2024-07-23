package com.thien.finance.identity_service.exception;

public class UserAlreadyRegisteredException extends SimpleBankingGlobalException {
    public UserAlreadyRegisteredException(String message, String code) {
        super(message, code);
    }
}
