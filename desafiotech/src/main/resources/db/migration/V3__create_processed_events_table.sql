CREATE TABLE processed_events (
                                  id UUID PRIMARY KEY,
                                  event_id UUID NOT NULL UNIQUE,
                                  event_type VARCHAR(80) NOT NULL,
                                  order_id UUID,
                                  processed_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_processed_events_event_id ON processed_events(event_id);
CREATE INDEX idx_processed_events_order_id ON processed_events(order_id);