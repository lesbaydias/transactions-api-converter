CREATE TABLE IF NOT EXISTS limits (
                                      limit_id BIGSERIAL PRIMARY KEY,
                                      limit_sum DECIMAL(19,2) NOT NULL,
    limit_date_time TIMESTAMP NOT NULL,
    limit_currency_short_name VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS transactions (
                                            id BIGSERIAL PRIMARY KEY,
                                            account_from BIGINT NOT NULL,
                                            account_to BIGINT NOT NULL,
                                            currency_short_name VARCHAR(255) NOT NULL,
    sum DECIMAL(19,2) NOT NULL,
    sum_in_usd DECIMAL(19,2) NOT NULL,
    expense_category_type VARCHAR(255) NOT NULL,
    date_time TIMESTAMP NOT NULL,
    limit_exceeded BOOLEAN NOT NULL
    );

CREATE TABLE IF NOT EXISTS currency_rate (
                                             id BIGSERIAL PRIMARY KEY,
                                             currency_pair VARCHAR(255) NOT NULL,
    close_rate DECIMAL(19,4) NOT NULL,
    date DATE NOT NULL
    );
