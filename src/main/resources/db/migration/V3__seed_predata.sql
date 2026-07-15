INSERT INTO application_users(id, created_by, updated_by, version, account_non_locked, email, enabled, password, role,
                              username)
VALUES (1, 'SYSTEM', 'SYSTEM', 0, true, 'portfolio.admin@example.com', true,
        '$2a$12$sZqcXvsuj/LiFlybQeAYwODg2hI9T9dyqZa/2cmjsqSATukBaQpNu', 'ADMIN', 'portfolio.admin')
    ON CONFLICT (id) DO NOTHING;

INSERT INTO investors (id, investor_code, full_name, email, country, risk_profile, committed_amount, created_at, created_by,
                       version)
VALUES (1, 'INV-001', 'Alpha Wealth Management', 'contact@alphawealth.com', 'Singapore', 'AGGRESSIVE', 5000000.00,
        NOW(), 'system', 0),
       (2, 'INV-002', 'Beatrix Vance', 'beatrix.v@example.com', 'United States', 'CONSERVATIVE', 250000.00, NOW(),
        'system', 0),
       (3, 'INV-003', 'Charles Dupont', 'c.dupont@eurocap.fr', 'France', 'MODERATE', 1200000.00, NOW(), 'system', 0),
       (4, 'INV-004', 'Dragon Capital Fund', 'invest@dragoncap.hk', 'Hong Kong', 'AGGRESSIVE', 10000000.00, NOW(),
        'system', 0),
       (5, 'INV-005', 'Elena Rostova', 'elena.r@rostovholdings.com', 'United Kingdom', 'MODERATE', 4500000.00, NOW(),
        'system', 0),
       (6, 'INV-006', 'Fukuda Asset Holdings', 'info@fukudaholdings.jp', 'Japan', 'CONSERVATIVE', 8000000.00, NOW(),
        'system', 0),
       (7, 'INV-007', 'George Miller', 'george.miller@gmail.com', 'Australia', 'MODERATE', 150000.00, NOW(), 'system',
        0),
       (8, 'INV-008', 'Horizon Family Office', 'ops@horizonfo.ch', 'Switzerland', 'AGGRESSIVE', 6500000.00, NOW(),
        'system', 0),
       (9, 'INV-009', 'Ibrahim Al-Mansoor', 'ibrahim@almansoor.ae', 'United Arab Emirates', 'MODERATE', 3000000.00,
        NOW(), 'system', 0),
       (10, 'INV-010', 'Jane Doe Capital', 'jane@janedoecapital.com', 'Canada', 'AGGRESSIVE', 750000.00, NOW(),
        'system', 0)
    ON CONFLICT (id) DO NOTHING;

INSERT INTO funds (id, fund_code, fund_name, asset_class, currency, nav, inception_date, status, created_at, created_by,
                   version)
VALUES (1, 'FND-PE-01', 'Global Tech Growth Fund I', 'PRIVATE_EQUITY', 'USD', 100.2500, '2025-01-15', 'ACTIVE', NOW(),
        'system', 0),
       (2, 'FND-RE-02', 'Euro Prime Real Estate', 'REAL_ESTATE', 'EUR', 12.4550, '2024-06-10', 'ACTIVE', NOW(),
        'system', 0),
       (3, 'FND-INF-03', 'APAC Infrastructure Fund', 'INFRASTRUCTURE', 'AUD', 1.0500, '2023-11-01', 'ACTIVE', NOW(),
        'system', 0),
       (4, 'FND-DEBT-04', 'SME Private Debt Pool', 'PRIVATE_DEBT', 'USD', 50.1100, '2025-03-20', 'ACTIVE', NOW(),
        'system', 0),
       (5, 'FND-HF-05', 'Sigma Quant Arbitrage', 'HEDGE_FUND', 'USD', 250.7890, '2022-05-18', 'ACTIVE', NOW(), 'system',
        0),
       (6, 'FND-PE-06', 'Vanguard Buyout Opportunities', 'PRIVATE_EQUITY', 'USD', 85.4000, '2024-09-05', 'ACTIVE',
        NOW(), 'system', 0),
       (7, 'FND-RE-07', 'Logistics Hub Core Property', 'REAL_ESTATE', 'EUR', 9.8700, '2025-02-11', 'ACTIVE', NOW(),
        'system', 0),
       (8, 'FND-INF-08', 'Green Energy Infrastructure', 'INFRASTRUCTURE', 'USD', 10.0000, '2026-01-01', 'ACTIVE', NOW(),
        'system', 0),
       (9, 'FND-DEBT-09', 'Nordic High Yield Debt', 'PRIVATE_DEBT', 'EUR', 103.6500, '2023-04-14', 'CLOSED', NOW(),
        'system', 0),
       (10, 'FND-HF-10', 'Alpha Macro Strategy', 'HEDGE_FUND', 'USD', 115.2000, '2024-12-25', 'SUSPENDED', NOW(),
        'system', 0)
    ON CONFLICT (id) DO NOTHING;

INSERT INTO investments (id, investor_id, fund_id, amount, nav, units, status, investment_date, created_at, created_by,
                         version)
VALUES (1, 1, 1, 500000.00, 100.2500, 4987.5312, 'CONFIRMED', '2026-07-01 10:00:00', NOW(), 'system', 0),
       (2, 2, 2, 50000.00, 12.4550, 4014.4520, 'CONFIRMED', '2026-07-02 11:30:00', NOW(), 'system', 0),
       (3, 3, 2, 250000.00, 12.4550, 20072.2601, 'VALIDATED', '2026-07-05 14:15:00', NOW(), 'system', 0),
       (4, 4, 4, 1000000.00, 50.1100, 19956.1006, 'CONFIRMED', '2026-07-06 09:00:00', NOW(), 'system', 0),
       (5, 5, 5, 300000.00, 250.7890, 1196.2247, 'PENDING', '2026-07-12 16:45:00', NOW(), 'system', 0),
       (6, 6, 3, 800000.00, 1.0500, 761904.7619, 'CONFIRMED', '2026-07-07 10:20:00', NOW(), 'system', 0),
       (7, 7, 1, 20000.00, 100.2500, 199.5012, 'FAILED', '2026-07-08 13:02:00', NOW(), 'system', 0),
       (8, 8, 6, 1500000.00, 85.4000, 17564.4028, 'VALIDATED', '2026-07-10 11:11:00', NOW(), 'system', 0),
       (9, 9, 8, 450000.00, 10.0000, 45000.0000, 'CANCELLED', '2026-07-11 15:30:00', NOW(), 'system', 0),
       (10, 10, 5, 100000.00, 250.7890, 398.7416, 'CONFIRMED', '2026-07-04 12:00:00', NOW(), 'system', 0)

ON CONFLICT (id) DO NOTHING;

-- Update underlying sequence numbers so future autoincrements don't clash
SELECT setval('investors_id_seq', (SELECT MAX(id) FROM investors));
SELECT setval('funds_id_seq', (SELECT MAX(id) FROM funds));
SELECT setval('investments_id_seq', (SELECT MAX(id) FROM investments));