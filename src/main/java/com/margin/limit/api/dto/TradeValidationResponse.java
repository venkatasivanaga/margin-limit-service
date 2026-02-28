package com.margin.limit.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TradeValidationResponse {
    private boolean valid;
    private String message;
}