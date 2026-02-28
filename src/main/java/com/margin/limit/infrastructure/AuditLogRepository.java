package com.margin.limit.infrastructure;

import com.margin.limit.domain.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends MongoRepository<AuditLog, String> {
    List<AuditLog> findByAccountIdOrderByTimestampDesc(UUID accountId);
}