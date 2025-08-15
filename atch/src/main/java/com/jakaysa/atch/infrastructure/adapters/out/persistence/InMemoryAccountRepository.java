package com.jakaysa.atch.infrastructure.adapters.out.persistence;

import com.jakaysa.atch.application.ports.out.AccountRepository;
import com.jakaysa.atch.domain.model.Account;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@Repository
public class InMemoryAccountRepository implements AccountRepository {

    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public Optional<Account> findById(String accountId) {
        return Optional.ofNullable(accounts.get(accountId));
    }

    @Override
    public Account save(Account account) {
        accounts.put(account.getId(), account);
        return account;
    }

    @Override
    public List<Account> findAll() {
        return List.copyOf(accounts.values());
    }

    @Override
    public void deleteAll() {
        accounts.clear();
    }

    @Override
    public boolean existsById(String accountId) {
        return accounts.containsKey(accountId);
    }
}
