package com.jakaysa.atch.application.ports.in;

import java.math.BigDecimal;
import java.util.Map;


public interface GetBalancesUseCase {
    

    Map<String, BigDecimal> getBalances();
}
