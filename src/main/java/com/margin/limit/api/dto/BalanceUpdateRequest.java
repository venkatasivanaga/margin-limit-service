package com.margin.limit.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceUpdateRequest {
    @NotNull(message = "Amount is required")
    private BigDecimal amount;
}