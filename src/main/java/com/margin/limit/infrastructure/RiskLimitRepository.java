package com.margin.limit.infrastructure;

import com.margin.limit.domain.LimitType;
import com.margin.limit.domain.RiskLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RiskLimitRepository extends JpaRepository<RiskLimit, UUID> {

    List<RiskLimit> findByAccountId(UUID accountId);

    Optional<RiskLimit> findByAccountIdAndLimitType(UUID accountId, LimitType limitType);

}