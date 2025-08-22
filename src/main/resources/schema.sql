CREATE TABLE transactions (
    transaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_type VARCHAR(20) NOT NULL,
    asset_symbol VARCHAR(20) NOT NULL,
    asset_name VARCHAR(100),
    asset_type VARCHAR(20) NOT NULL,
    quantity DECIMAL(19, 8),
    price_per_unit DECIMAL(19, 4),
    total_amount DECIMAL(19, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    exchange VARCHAR(50),
    created_at DATE NOT NULL,
    date DATE NOT NULL
);