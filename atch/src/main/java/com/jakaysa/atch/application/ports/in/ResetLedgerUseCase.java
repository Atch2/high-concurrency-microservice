package com.jakaysa.atch.application.ports.in;

import java.math.BigDecimal;


public interface ResetLedgerUseCase {
    

    void resetLedger(ResetLedgerCommand command);
    

    record ResetLedgerCommand(
        int numberOfAccounts,
        BigDecimal initialBalance
    ) {
        public ResetLedgerCommand {
            if (numberOfAccounts <= 0) {
                throw new IllegalArgumentException("numberOfAccounts debe ser mayor a cero");
            }
            if (initialBalance == null || initialBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("initialBalance no puede ser nulo o negativo");
            }
        }
    }
}
