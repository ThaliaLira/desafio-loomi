CREATE TABLE orders (
                        id UUID PRIMARY KEY,
                        customer_id VARCHAR(255) NOT NULL,
                        status VARCHAR(50) NOT NULL,
                        total_amount NUMERIC(10,2),
                        created_at TIMESTAMP
);