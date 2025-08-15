package com.jakaysa.atch.application.ports.out;

import com.jakaysa.atch.domain.model.Account;

import java.util.List;
import java.util.Optional;


public interface AccountRepository {
    

    Optional<Account> findById(String accountId);
    

    Account save(Account account);
    

    List<Account> findAll();
    

    void deleteAll();
    

    boolean existsById(String accountId);
}
