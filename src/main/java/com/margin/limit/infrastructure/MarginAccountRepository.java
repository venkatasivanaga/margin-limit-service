package com.margin.limit.infrastructure;

import com.margin.limit.domain.MarginAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MarginAccountRepository extends JpaRepository<MarginAccount, UUID> {
    
    Optional<MarginAccount> findByClientId(String clientId);
    
}