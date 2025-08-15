package com.jakaysa.atch.infrastructure.adapters.in.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class TransferResponse {
    private boolean ok;
    private long tookMs;
}
