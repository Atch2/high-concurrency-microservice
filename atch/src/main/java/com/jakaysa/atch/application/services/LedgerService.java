package com.jakaysa.atch.application.services;

import com.jakaysa.atch.application.ports.in.GetBalancesUseCase;
import com.jakaysa.atch.application.ports.in.ResetLedgerUseCase;
import com.jakaysa.atch.application.ports.in.TransferMoneyUseCase;
import com.jakaysa.atch.application.ports.out.AccountRepository;
import com.jakaysa.atch.application.ports.out.IdempotencyRepository;
import com.jakaysa.atch.domain.exceptions.AccountNotFoundException;
import com.jakaysa.atch.domain.exceptions.InsufficientFundsException;
import com.jakaysa.atch.domain.model.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class LedgerService implements TransferMoneyUseCase, GetBalancesUseCase, ResetLedgerUseCase {

    private final AccountRepository accountRepository;
    private final IdempotencyRepository idempotencyRepository;
    
    private final ConcurrentHashMap<String, ReentrantLock> accountLocks = new ConcurrentHashMap<>();

    @Override
    public void transferMoney(TransferMoneyCommand command) {
        log.debug("Iniciando transferencia: {} -> {}, monto: {}, key: {}", 
                  command.fromAccountId(), command.toAccountId(), command.amount(), command.idempotencyKey());

        if (idempotencyRepository.wasProcessed(command.idempotencyKey())) {
            log.debug("Transferencia ya procesada con key: {}", command.idempotencyKey());
            return;
        }

        String firstAccount = command.fromAccountId().compareTo(command.toAccountId()) < 0 
                              ? command.fromAccountId() : command.toAccountId();
        String secondAccount = command.fromAccountId().equals(firstAccount) 
                               ? command.toAccountId() : command.fromAccountId();

        ReentrantLock firstLock = getAccountLock(firstAccount);
        ReentrantLock secondLock = getAccountLock(secondAccount);

        firstLock.lock();
        try {
            secondLock.lock();
            try {
                if (idempotencyRepository.wasProcessed(command.idempotencyKey())) {
                    log.debug("Transferencia ya procesada con key: {} (verificación dentro del lock)", 
                              command.idempotencyKey());
                    return;
                }

                performTransfer(command);
                
            } finally {
                secondLock.unlock();
            }
        } finally {
            firstLock.unlock();
        }
    }

    private void performTransfer(TransferMoneyCommand command) {
        Account fromAccount = accountRepository.findById(command.fromAccountId())
                .orElseThrow(() -> new AccountNotFoundException(command.fromAccountId()));
        
        Account toAccount = accountRepository.findById(command.toAccountId())
                .orElseThrow(() -> new AccountNotFoundException(command.toAccountId()));

        if (!fromAccount.hasSufficientFunds(command.amount())) {
            throw new InsufficientFundsException(command.fromAccountId());
        }

        fromAccount.debit(command.amount());
        toAccount.credit(command.amount());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        idempotencyRepository.markAsProcessed(command.idempotencyKey());

        log.debug("Transferencia completada exitosamente: {} -> {}, monto: {}", 
                  command.fromAccountId(), command.toAccountId(), command.amount());
    }

    @Override
    public Map<String, BigDecimal> getBalances() {
        return accountRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        Account::getId,
                        Account::getBalance
                ));
    }

    @Override
    public void resetLedger(ResetLedgerCommand command) {
        log.info("Reiniciando ledger con {} cuentas y balance inicial de {}", 
                 command.numberOfAccounts(), command.initialBalance());

        accountRepository.deleteAll();
        idempotencyRepository.clear();
        accountLocks.clear();

        for (int i = 0; i < command.numberOfAccounts(); i++) {
            String accountId = "A" + i;
            Account account = new Account(accountId, command.initialBalance());
            accountRepository.save(account);
        }

        log.info("Ledger reiniciado exitosamente");
    }

    private ReentrantLock getAccountLock(String accountId) {
        return accountLocks.computeIfAbsent(accountId, k -> new ReentrantLock());
    }
}
