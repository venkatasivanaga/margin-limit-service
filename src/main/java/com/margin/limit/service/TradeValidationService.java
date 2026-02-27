package com.margin.limit.service;

import com.margin.limit.api.dto.TradeRequest;
import com.margin.limit.domain.LimitType;
import com.margin.limit.domain.MarginAccount;
import com.margin.limit.domain.RiskLimit;
import com.margin.limit.infrastructure.MarginAccountRepository;
import com.margin.limit.infrastructure.RiskLimitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TradeValidationService {

    private final MarginAccountRepository accountRepository;
    private final RiskLimitRepository riskLimitRepository;

    @Transactional(readOnly = true)
    public boolean validateTrade(TradeRequest request) {
        // 1. Check if account exists and has enough cash balance
        MarginAccount account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + request.getAccountId()));

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            return false; // Insufficient base balance
        }

        // 2. Check Gross Exposure Limit (if one exists for the account)
        Optional<RiskLimit> grossExposureLimit = riskLimitRepository.findByAccountIdAndLimitType(
                request.getAccountId(), LimitType.MAX_GROSS_EXPOSURE);

        if (grossExposureLimit.isPresent()) {
            RiskLimit limit = grossExposureLimit.get();
            BigDecimal projectedUtilization = limit.getCurrentUtilization().add(request.getAmount());
            
            if (projectedUtilization.compareTo(limit.getMaxLimitValue()) > 0) {
                return false; // Trade would breach the risk limit
            }
        }

        // If all checks pass, the trade is valid
        return true;
    }
}