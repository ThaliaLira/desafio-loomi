CREATE TABLE digital_product_ownership (
                                           id UUID PRIMARY KEY,
                                           customer_id VARCHAR(255) NOT NULL,
                                           product_id VARCHAR(100) NOT NULL,
                                           license_key VARCHAR(255) NOT NULL,
                                           created_at TIMESTAMP NOT NULL
);

CREATE UNIQUE INDEX uq_customer_digital_product
    ON digital_product_ownership(customer_id, product_id);