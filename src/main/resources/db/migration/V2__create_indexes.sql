CREATE INDEX idx_investors_country
    ON investors (country);

CREATE INDEX idx_investors_risk_profile
    ON investors (risk_profile);

CREATE INDEX idx_funds_status
    ON funds (status);

CREATE INDEX idx_funds_asset_class
    ON funds (asset_class);

CREATE INDEX idx_funds_currency
    ON funds (currency);

CREATE INDEX idx_investments_investor_id
    ON investments (investor_id);

CREATE INDEX idx_investments_fund_id
    ON investments (fund_id);

CREATE INDEX idx_investments_status
    ON investments (status);

CREATE INDEX idx_investments_investment_date
    ON investments (investment_date);

CREATE INDEX idx_investments_investor_status
    ON investments (investor_id, status);

CREATE INDEX idx_investments_fund_status
    ON investments (fund_id, status);