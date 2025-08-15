package com.jakaysa.atch.domain.exceptions;


public class InvalidTransferException extends DomainException {
    public InvalidTransferException(String message) {
        super("Transferencia inválida: " + message);
    }
}
