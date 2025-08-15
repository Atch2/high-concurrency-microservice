package com.jakaysa.atch.domain.exceptions;


public class AccountNotFoundException extends DomainException {
    public AccountNotFoundException(String accountId) {
        super("La cuenta " + accountId + " no existe");
    }
}
