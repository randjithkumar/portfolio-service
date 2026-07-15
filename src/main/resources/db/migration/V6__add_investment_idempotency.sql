ALTER TABLE investments
    ADD COLUMN idempotency_key VARCHAR(100);

UPDATE investments
SET idempotency_key = 'legacy-' || id
WHERE idempotency_key IS NULL;

ALTER TABLE investments
    ALTER COLUMN idempotency_key SET NOT NULL;

ALTER TABLE investments
    ADD CONSTRAINT uk_investments_idempotency_key UNIQUE (idempotency_key);