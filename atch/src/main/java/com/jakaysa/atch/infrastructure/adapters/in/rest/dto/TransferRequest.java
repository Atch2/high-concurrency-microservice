package com.jakaysa.atch.infrastructure.adapters.in.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    private String from;
    private String to;
    private BigDecimal amount;
    private String key;
}
