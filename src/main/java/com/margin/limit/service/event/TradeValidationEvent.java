package com.margin.limit.service.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class TradeValidationEvent {
    private final UUID accountId;
    private final String symbol;
    private final BigDecimal amount;
    private final boolean approved;
    private final String reason;
}