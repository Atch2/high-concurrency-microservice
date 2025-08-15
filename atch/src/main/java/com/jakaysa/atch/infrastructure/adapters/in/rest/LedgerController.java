package com.jakaysa.atch.infrastructure.adapters.in.rest;

import com.jakaysa.atch.application.ports.in.GetBalancesUseCase;
import com.jakaysa.atch.application.ports.in.ResetLedgerUseCase;
import com.jakaysa.atch.application.ports.in.TransferMoneyUseCase;
import com.jakaysa.atch.domain.exceptions.AccountNotFoundException;
import com.jakaysa.atch.domain.exceptions.DomainException;
import com.jakaysa.atch.domain.exceptions.InsufficientFundsException;
import com.jakaysa.atch.infrastructure.adapters.in.rest.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@Slf4j
public class LedgerController {

    private final TransferMoneyUseCase transferMoneyUseCase;
    private final GetBalancesUseCase getBalancesUseCase;
    private final ResetLedgerUseCase resetLedgerUseCase;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/reset")
    public ResponseEntity<Map<String, Boolean>> reset(@RequestBody(required = false) ResetRequest request) {
        try {
            if (request == null) {
                request = new ResetRequest();
            }
            
            ResetLedgerUseCase.ResetLedgerCommand command = new ResetLedgerUseCase.ResetLedgerCommand(
                    request.getAccounts(),
                    request.getInitialBalance()
            );
            
            resetLedgerUseCase.resetLedger(command);
            
            return ResponseEntity.ok(Map.of("ok", true));
            
        } catch (IllegalArgumentException e) {
            log.warn("Bad request en reset: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("ok", false));
        } catch (Exception e) {
            log.error("Error interno en reset", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("ok", false));
        }
    }

    @GetMapping("/balances")
    public ResponseEntity<Map<String, BigDecimal>> getBalances() {
        try {
            Map<String, BigDecimal> balances = getBalancesUseCase.getBalances();
            return ResponseEntity.ok(balances);
        } catch (Exception e) {
            log.error("Error obteniendo balances", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request) {
        long startTime = System.currentTimeMillis();
        
        try {
            if (request == null || request.getFrom() == null || request.getTo() == null ||
                request.getAmount() == null || request.getKey() == null) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("bad request"));
            }

            TransferMoneyUseCase.TransferMoneyCommand command = 
                    new TransferMoneyUseCase.TransferMoneyCommand(
                            request.getFrom(),
                            request.getTo(),
                            request.getAmount(),
                            request.getKey()
                    );

            transferMoneyUseCase.transferMoney(command);

            long duration = System.currentTimeMillis() - startTime;
            return ResponseEntity.ok(new TransferResponse(true, duration));

        } catch (IllegalArgumentException e) {
            log.warn("Bad request en transfer: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("bad request"));
                    
        } catch (AccountNotFoundException e) {
            log.warn("Cuenta no encontrada: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("unknown account"));
                    
        } catch (InsufficientFundsException e) {
            log.warn("Fondos insuficientes: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("insufficient funds"));
                    
        } catch (DomainException e) {
            log.warn("Error de dominio: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(e.getMessage()));
                    
        } catch (Exception e) {
            log.error("Error interno en transfer", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("transfer failed"));
        }
    }
}
