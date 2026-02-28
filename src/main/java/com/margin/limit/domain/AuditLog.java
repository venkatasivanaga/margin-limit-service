package com.margin.limit.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@Document(collection = "trade_validation_audits")
public class AuditLog {
    @Id
    private String id;
    private UUID accountId;
    private String symbol;
    private String tradeAmount; // Storing as String to prevent floating-point precision loss in NoSQL
    private boolean approved;
    private String reason;
    private Instant timestamp;
}