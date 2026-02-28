package com.margin.limit.api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountRequest {
    @NotBlank(message = "Client ID is required")
    private String clientId;

    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be a 3-letter code")
    private String currency;

    @NotNull(message = "Initial balance is required")
    @PositiveOrZero(message = "Initial balance cannot be negative")
    private BigDecimal initialBalance;
}