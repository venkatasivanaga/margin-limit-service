package com.margin.limit.service;

import com.margin.limit.domain.AccountStatus;
import com.margin.limit.domain.MarginAccount;
import com.margin.limit.infrastructure.MarginAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final MarginAccountRepository accountRepository;

    @Transactional
    public MarginAccount createAccount(String clientId, String currency, BigDecimal initialBalance) {
        if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }

        MarginAccount account = MarginAccount.builder()
                .clientId(clientId)
                .currency(currency)
                .balance(initialBalance)
                .status(AccountStatus.ACTIVE)
                .build();

        return accountRepository.save(account);
    }

    public MarginAccount getAccountByClientId(String clientId) {
        return accountRepository.findByClientId(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found for client: " + clientId));
    }

    @Transactional
    public MarginAccount updateBalance(UUID accountId, BigDecimal amount) {
        MarginAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));

        BigDecimal newBalance = account.getBalance().add(amount);
        
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient funds. Cannot reduce balance below zero.");
        }

        account.setBalance(newBalance);
        return accountRepository.save(account);
    }
}