package com.margin.limit.service;

import com.margin.limit.domain.AccountStatus;
import com.margin.limit.domain.MarginAccount;
import com.margin.limit.infrastructure.MarginAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private MarginAccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccount_WithValidData_SavesAndReturnsAccount() {
        // Arrange
        String clientId = "CLIENT_123";
        BigDecimal balance = new BigDecimal("10000.00");
        MarginAccount mockAccount = MarginAccount.builder()
                .id(UUID.randomUUID())
                .clientId(clientId)
                .balance(balance)
                .currency("USD")
                .status(AccountStatus.ACTIVE)
                .build();

        when(accountRepository.save(any(MarginAccount.class))).thenReturn(mockAccount);

        // Act
        MarginAccount created = accountService.createAccount(clientId, "USD", balance);

        // Assert
        assertNotNull(created);
        assertEquals(clientId, created.getClientId());
        assertEquals(balance, created.getBalance());
        verify(accountRepository, times(1)).save(any(MarginAccount.class));
    }

    @Test
    void updateBalance_WithSufficientFunds_UpdatesSuccessfully() {
        // Arrange
        UUID accountId = UUID.randomUUID();
        MarginAccount existingAccount = new MarginAccount();
        existingAccount.setId(accountId);
        existingAccount.setBalance(new BigDecimal("500.00"));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(any(MarginAccount.class))).thenReturn(existingAccount);

        // Act
        MarginAccount updated = accountService.updateBalance(accountId, new BigDecimal("-200.00"));

        // Assert
        assertEquals(new BigDecimal("300.00"), updated.getBalance());
        verify(accountRepository, times(1)).save(existingAccount);
    }

    @Test
    void updateBalance_WithInsufficientFunds_ThrowsException() {
        // Arrange
        UUID accountId = UUID.randomUUID();
        MarginAccount existingAccount = new MarginAccount();
        existingAccount.setId(accountId);
        existingAccount.setBalance(new BigDecimal("100.00"));

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.updateBalance(accountId, new BigDecimal("-150.00"));
        });

        assertEquals("Insufficient funds. Cannot reduce balance below zero.", exception.getMessage());
        verify(accountRepository, never()).save(any());
    }
}