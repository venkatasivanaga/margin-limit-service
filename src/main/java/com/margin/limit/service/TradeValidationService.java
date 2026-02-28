package com.margin.limit.service;

import com.margin.limit.api.dto.TradeRequest;
import com.margin.limit.domain.LimitType;
import com.margin.limit.domain.MarginAccount;
import com.margin.limit.domain.RiskLimit;
import com.margin.limit.infrastructure.MarginAccountRepository;
import com.margin.limit.infrastructure.RiskLimitRepository;
import com.margin.limit.service.event.TradeValidationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TradeValidationService {

    private final MarginAccountRepository accountRepository;
    private final RiskLimitRepository riskLimitRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public boolean validateTrade(TradeRequest request) {
        MarginAccount account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + request.getAccountId()));

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            publishEvent(request, false, "Insufficient base balance");
            return false;
        }

        Optional<RiskLimit> grossExposureLimit = riskLimitRepository.findByAccountIdAndLimitType(
                request.getAccountId(), LimitType.MAX_GROSS_EXPOSURE);

        if (grossExposureLimit.isPresent()) {
            RiskLimit limit = grossExposureLimit.get();
            BigDecimal projectedUtilization = limit.getCurrentUtilization().add(request.getAmount());
            
            if (projectedUtilization.compareTo(limit.getMaxLimitValue()) > 0) {
                publishEvent(request, false, "Trade would breach MAX_GROSS_EXPOSURE limit");
                return false;
            }
        }

        publishEvent(request, true, "Trade approved");
        return true;
    }

    private void publishEvent(TradeRequest request, boolean approved, String reason) {
        eventPublisher.publishEvent(new TradeValidationEvent(
                request.getAccountId(),
                request.getSymbol(),
                request.getAmount(),
                approved,
                reason
        ));
    }
}