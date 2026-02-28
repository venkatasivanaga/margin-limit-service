package com.margin.limit.api;

import com.margin.limit.api.dto.TradeRequest;
import com.margin.limit.api.dto.TradeValidationResponse;
import com.margin.limit.service.TradeValidationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trades")
@RequiredArgsConstructor
public class TradeValidationController {

    private final TradeValidationService validationService;

    @PostMapping("/validate")
    public ResponseEntity<TradeValidationResponse> validateTrade(@Valid @RequestBody TradeRequest request) {
        boolean isValid = validationService.validateTrade(request);
        
        String message = isValid 
                ? "Trade approved." 
                : "Trade rejected: Insufficient margin or risk limit breached.";
                
        return ResponseEntity.ok(new TradeValidationResponse(isValid, message));
    }
}