package com.margin.limit.service.event;

import com.margin.limit.domain.AuditLog;
import com.margin.limit.infrastructure.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditEventListener {

    private final AuditLogRepository auditLogRepository;

    @Async
    @EventListener
    public void handleTradeValidationEvent(TradeValidationEvent event) {
        log.info("Asynchronously logging trade validation for account: {}", event.getAccountId());
        
        AuditLog auditLog = AuditLog.builder()
                .accountId(event.getAccountId())
                .symbol(event.getSymbol())
                .tradeAmount(event.getAmount().toPlainString())
                .approved(event.isApproved())
                .reason(event.getReason())
                .timestamp(Instant.now())
                .build();
                
        auditLogRepository.save(auditLog);
    }
}