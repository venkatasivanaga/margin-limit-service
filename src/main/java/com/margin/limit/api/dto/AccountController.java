package com.margin.limit.api;

import com.margin.limit.api.dto.BalanceUpdateRequest;
import com.margin.limit.api.dto.CreateAccountRequest;
import com.margin.limit.domain.MarginAccount;
import com.margin.limit.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<MarginAccount> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        MarginAccount account = accountService.createAccount(
                request.getClientId(), 
                request.getCurrency(), 
                request.getInitialBalance()
        );
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<MarginAccount> getAccountByClientId(@PathVariable String clientId) {
        return ResponseEntity.ok(accountService.getAccountByClientId(clientId));
    }

    @PatchMapping("/{accountId}/balance")
    public ResponseEntity<MarginAccount> updateBalance(
            @PathVariable UUID accountId,
            @Valid @RequestBody BalanceUpdateRequest request) {
        return ResponseEntity.ok(accountService.updateBalance(accountId, request.getAmount()));
    }
}