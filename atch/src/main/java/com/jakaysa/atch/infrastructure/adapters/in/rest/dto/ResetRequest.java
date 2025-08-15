package com.jakaysa.atch.infrastructure.adapters.in.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetRequest {
    private Integer accounts = 100;
    private BigDecimal initialBalance = BigDecimal.valueOf(1000);
}
