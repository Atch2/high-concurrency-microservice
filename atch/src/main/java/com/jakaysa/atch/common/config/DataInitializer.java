package com.jakaysa.atch.common.config;

import com.jakaysa.atch.application.ports.in.ResetLedgerUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ResetLedgerUseCase resetLedgerUseCase;

    @Override
    public void run(String... args) {
        log.info("Inicializando datos del ledger...");
        
        ResetLedgerUseCase.ResetLedgerCommand command = 
                new ResetLedgerUseCase.ResetLedgerCommand(100, BigDecimal.valueOf(1000));
        
        resetLedgerUseCase.resetLedger(command);
        
        log.info("Datos del ledger inicializados correctamente");
    }
}
