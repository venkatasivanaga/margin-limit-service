package com.margin.limit.service;

import com.margin.limit.api.dto.TradeRequest;
import com.margin.limit.domain.AccountStatus;
import com.margin.limit.domain.LimitType;
import com.margin.limit.domain.MarginAccount;
import com.margin.limit.domain.RiskLimit;
import com.margin.limit.infrastructure.MarginAccountRepository;
import com.margin.limit.infrastructure.RiskLimitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
class TradeValidationIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Container
    @ServiceConnection
    static MongoDBContainer mongo = new MongoDBContainer("mongo:6.0");

    @Autowired
    private TradeValidationService validationService;

    @Autowired
    private MarginAccountRepository accountRepository;

    @Autowired
    private RiskLimitRepository riskLimitRepository;

    private MarginAccount testAccount;

    @BeforeEach
    void setUp() {
        riskLimitRepository.deleteAll();
        accountRepository.deleteAll();

        testAccount = MarginAccount.builder()
                .clientId("INT_TEST_CLIENT")
                .balance(new BigDecimal("10000.00"))
                .currency("USD")
                .status(AccountStatus.ACTIVE)
                .build();
        testAccount = accountRepository.save(testAccount);
    }

    @Test
    void shouldApproveValidTrade() {
        TradeRequest request = new TradeRequest();
        request.setAccountId(testAccount.getId());
        request.setSymbol("AAPL");
        request.setAmount(new BigDecimal("5000.00"));

        boolean result = validationService.validateTrade(request);
        assertTrue(result);
    }

    @Test
    void shouldRejectTradeExceedingBalance() {
        TradeRequest request = new TradeRequest();
        request.setAccountId(testAccount.getId());
        request.setSymbol("TSLA");
        request.setAmount(new BigDecimal("15000.00"));

        boolean result = validationService.validateTrade(request);
        assertFalse(result);
    }

    @Test
    void shouldRejectTradeExceedingGrossExposureLimit() {
        RiskLimit limit = RiskLimit.builder()
                .account(testAccount)
                .limitType(LimitType.MAX_GROSS_EXPOSURE)
                .maxLimitValue(new BigDecimal("8000.00"))
                .currentUtilization(new BigDecimal("4000.00"))
                .build();
        riskLimitRepository.save(limit);

        TradeRequest request = new TradeRequest();
        request.setAccountId(testAccount.getId());
        request.setSymbol("GOOGL");
        request.setAmount(new BigDecimal("5000.00")); // 4000 + 5000 = 9000 > 8000 limit

        boolean result = validationService.validateTrade(request);
        assertFalse(result);
    }
}