CREATE TABLE margin_accounts (
    id UUID PRIMARY KEY,
    client_id VARCHAR(255) NOT NULL UNIQUE,
    balance DECIMAL(19, 4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE risk_limits (
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL,
    limit_type VARCHAR(50) NOT NULL,
    max_limit_value DECIMAL(19, 4) NOT NULL,
    current_utilization DECIMAL(19, 4) NOT NULL DEFAULT 0.0000,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES margin_accounts (id) ON DELETE CASCADE,
    CONSTRAINT uq_account_limit UNIQUE (account_id, limit_type)
);