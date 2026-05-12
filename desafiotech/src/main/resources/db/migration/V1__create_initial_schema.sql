CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE TABLE products (
                          id UUID PRIMARY KEY,
                          product_id VARCHAR(50) NOT NULL UNIQUE,
                          name VARCHAR(255) NOT NULL,
                          product_type VARCHAR(30) NOT NULL,
                          price NUMERIC(10, 2) NOT NULL,
                          stock_quantity INTEGER,
                          active BOOLEAN NOT NULL DEFAULT TRUE,
                          metadata TEXT,
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP NOT NULL
);

CREATE TABLE orders (
                        id UUID PRIMARY KEY,
                        customer_id VARCHAR(255) NOT NULL,
                        status VARCHAR(50) NOT NULL,
                        failure_reason VARCHAR(80),
                        total_amount NUMERIC(10, 2) NOT NULL,
                        created_at TIMESTAMP NOT NULL,
                        updated_at TIMESTAMP NOT NULL
);

CREATE TABLE order_items (
                             id UUID PRIMARY KEY,
                             order_id UUID NOT NULL,
                             product_id VARCHAR(50) NOT NULL,
                             product_name VARCHAR(255) NOT NULL,
                             product_type VARCHAR(30) NOT NULL,
                             quantity INTEGER NOT NULL,
                             unit_price NUMERIC(10, 2) NOT NULL,
                             total_price NUMERIC(10, 2) NOT NULL,
                             metadata TEXT,
                             created_at TIMESTAMP NOT NULL,
                             updated_at TIMESTAMP NOT NULL,

                             CONSTRAINT fk_order_items_order
                                 FOREIGN KEY (order_id)
                                     REFERENCES orders(id)
);

CREATE INDEX idx_orders_customer_id ON orders(customer_id);
CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_products_product_id ON products(product_id);