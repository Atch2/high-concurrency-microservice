package com.jakaysa.atch.application.ports.in;

import java.math.BigDecimal;


public interface TransferMoneyUseCase {
    

    void transferMoney(TransferMoneyCommand request);
    

    record TransferMoneyCommand(
        String fromAccountId,
        String toAccountId,
        BigDecimal amount,
        String idempotencyKey
    ) {
        public TransferMoneyCommand {
            if (fromAccountId == null || fromAccountId.trim().isEmpty()) {
                throw new IllegalArgumentException("fromAccountId no puede ser nulo o vacío");
            }
            if (toAccountId == null || toAccountId.trim().isEmpty()) {
                throw new IllegalArgumentException("toAccountId no puede ser nulo o vacío");
            }
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("amount debe ser mayor a cero");
            }
            if (idempotencyKey == null || idempotencyKey.trim().isEmpty()) {
                throw new IllegalArgumentException("idempotencyKey no puede ser nulo o vacío");
            }
            if (fromAccountId.equals(toAccountId)) {
                throw new IllegalArgumentException("Las cuentas de origen y destino no pueden ser iguales");
            }
        }
    }
}
