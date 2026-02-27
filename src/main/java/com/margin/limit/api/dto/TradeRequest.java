package com.margin.limit.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TradeRequest {
    @NotNull(message = "Account ID is required")
    private UUID accountId;

    @NotBlank(message = "Asset symbol is required")
    private String symbol;

    @NotNull(message = "Trade amount is required")
    @Positive(message = "Trade amount must be greater than zero")
    private BigDecimal amount;
}