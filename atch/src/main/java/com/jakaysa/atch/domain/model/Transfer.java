package com.jakaysa.atch.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;


@Getter
@AllArgsConstructor
public class Transfer {
    private final String key;
    private final String fromAccountId;
    private final String toAccountId;
    private final BigDecimal amount;
    private final Instant timestamp;


    public boolean isValid() {
        return key != null && !key.trim().isEmpty() &&
               fromAccountId != null && !fromAccountId.trim().isEmpty() &&
               toAccountId != null && !toAccountId.trim().isEmpty() &&
               amount != null && amount.compareTo(BigDecimal.ZERO) > 0 &&
               !fromAccountId.equals(toAccountId);
    }
}
