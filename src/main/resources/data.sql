INSERT INTO transactions (
    transaction_type, asset_symbol, asset_name, asset_type, quantity, price_per_unit, total_amount, currency, exchange, created_at, date
) VALUES
('BUY', 'AAPL', 'Apple Inc.', 'STOCK', 10.5, 150.25, 1577.63, 'USD', 'NASDAQ', '2025-08-21', '2025-08-20'),
('SELL', 'GOOGL', 'Alphabet Inc.', 'STOCK', 5.0, 2800.00, 14000.00, 'USD', 'NASDAQ', '2025-08-21', '2025-08-19'),
('BUY', 'BTC', 'Bitcoin', 'CRYPTO', 0.25, 40000.00, 10000.00, 'USD', 'COINBASE', '2025-08-21', '2025-08-18'),
('BUY', 'TSLA', 'Tesla Inc.', 'STOCK', 3.0, 700.00, 2100.00, 'USD', 'NASDAQ', '2025-08-21', '2025-08-17'),
('SELL', 'ETH', 'Ethereum', 'CRYPTO', 1.0, 2500.00, 2500.00, 'USD', 'BINANCE', '2025-08-21', '2025-08-16');
