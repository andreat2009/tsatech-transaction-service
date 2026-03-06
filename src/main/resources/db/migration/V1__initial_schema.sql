CREATE TABLE store_transaction (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    order_id BIGINT,
    description VARCHAR(255) NOT NULL,
    amount NUMERIC(14,2) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_store_transaction_customer ON store_transaction(customer_id);
CREATE INDEX idx_store_transaction_created_at ON store_transaction(created_at DESC);
