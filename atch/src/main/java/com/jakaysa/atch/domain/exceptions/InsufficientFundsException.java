package com.jakaysa.atch.domain.exceptions;


public class InsufficientFundsException extends DomainException {
    public InsufficientFundsException(String accountId) {
        super("La cuenta " + accountId + " no tiene fondos suficientes");
    }
}
