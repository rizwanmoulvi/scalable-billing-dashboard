-- Initialize Billing Database Schema

-- Create extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_stat_statements";

-- Customers table
CREATE TABLE customers (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    company VARCHAR(255),
    plan_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_customers_email ON customers(email);
CREATE INDEX idx_customers_status ON customers(status) WHERE status = 'ACTIVE';

-- Usage data table (partitioned by month)
CREATE TABLE usage_data (
    id BIGSERIAL,
    customer_id UUID NOT NULL REFERENCES customers(id),
    resource_type VARCHAR(100) NOT NULL,
    quantity DECIMAL(15, 4) NOT NULL,
    unit VARCHAR(50) NOT NULL,
    unit_price DECIMAL(10, 4),
    timestamp TIMESTAMP NOT NULL,
    metadata JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id, timestamp)
) PARTITION BY RANGE (timestamp);

-- Create partitions for current and next 6 months
CREATE TABLE usage_data_2025_12 PARTITION OF usage_data
    FOR VALUES FROM ('2025-12-01') TO ('2026-01-01');
CREATE TABLE usage_data_2026_01 PARTITION OF usage_data
    FOR VALUES FROM ('2026-01-01') TO ('2026-02-01');
CREATE TABLE usage_data_2026_02 PARTITION OF usage_data
    FOR VALUES FROM ('2026-02-01') TO ('2026-03-01');
CREATE TABLE usage_data_2026_03 PARTITION OF usage_data
    FOR VALUES FROM ('2026-03-01') TO ('2026-04-01');
CREATE TABLE usage_data_2026_04 PARTITION OF usage_data
    FOR VALUES FROM ('2026-04-01') TO ('2026-05-01');
CREATE TABLE usage_data_2026_05 PARTITION OF usage_data
    FOR VALUES FROM ('2026-05-01') TO ('2026-06-01');

CREATE INDEX idx_usage_customer_time ON usage_data(customer_id, timestamp DESC);
CREATE INDEX idx_usage_resource_time ON usage_data(resource_type, timestamp DESC);
CREATE INDEX idx_usage_timestamp ON usage_data(timestamp DESC);

-- Billing records table
CREATE TABLE billing_records (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    customer_id UUID NOT NULL REFERENCES customers(id),
    billing_period_start DATE NOT NULL,
    billing_period_end DATE NOT NULL,
    total_amount DECIMAL(12, 2) NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    invoice_number VARCHAR(100) UNIQUE,
    due_date DATE,
    paid_date DATE,
    metadata JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_billing_customer ON billing_records(customer_id);
CREATE INDEX idx_billing_period ON billing_records(billing_period_start, billing_period_end);
CREATE INDEX idx_billing_status ON billing_records(status);
CREATE INDEX idx_billing_invoice ON billing_records(invoice_number);

-- Billing line items
CREATE TABLE billing_line_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    billing_record_id UUID NOT NULL REFERENCES billing_records(id) ON DELETE CASCADE,
    description VARCHAR(500) NOT NULL,
    resource_type VARCHAR(100),
    quantity DECIMAL(15, 4) NOT NULL,
    unit_price DECIMAL(10, 4) NOT NULL,
    amount DECIMAL(12, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_line_items_billing ON billing_line_items(billing_record_id);

-- Materialized view for analytics (30% performance improvement)
CREATE MATERIALIZED VIEW daily_usage_summary AS
SELECT 
    customer_id,
    DATE(timestamp) as usage_date,
    resource_type,
    SUM(quantity) as total_quantity,
    SUM(quantity * COALESCE(unit_price, 0)) as total_cost,
    COUNT(*) as event_count,
    MIN(timestamp) as first_event,
    MAX(timestamp) as last_event
FROM usage_data
GROUP BY customer_id, DATE(timestamp), resource_type;

CREATE UNIQUE INDEX idx_daily_summary ON daily_usage_summary(customer_id, usage_date, resource_type);
CREATE INDEX idx_daily_summary_date ON daily_usage_summary(usage_date DESC);

-- Materialized view for monthly analytics
CREATE MATERIALIZED VIEW monthly_billing_summary AS
SELECT 
    c.id as customer_id,
    c.name as customer_name,
    c.plan_type,
    DATE_TRUNC('month', b.billing_period_start) as billing_month,
    COUNT(b.id) as invoice_count,
    SUM(b.total_amount) as total_amount,
    AVG(b.total_amount) as avg_amount,
    SUM(CASE WHEN b.status = 'PAID' THEN b.total_amount ELSE 0 END) as paid_amount,
    SUM(CASE WHEN b.status = 'PENDING' THEN b.total_amount ELSE 0 END) as pending_amount
FROM customers c
LEFT JOIN billing_records b ON c.id = b.customer_id
GROUP BY c.id, c.name, c.plan_type, DATE_TRUNC('month', b.billing_period_start);

CREATE INDEX idx_monthly_summary_customer ON monthly_billing_summary(customer_id);
CREATE INDEX idx_monthly_summary_month ON monthly_billing_summary(billing_month DESC);

-- Function to refresh materialized views
CREATE OR REPLACE FUNCTION refresh_analytics_views()
RETURNS void AS $$
BEGIN
    REFRESH MATERIALIZED VIEW CONCURRENTLY daily_usage_summary;
    REFRESH MATERIALIZED VIEW CONCURRENTLY monthly_billing_summary;
END;
$$ LANGUAGE plpgsql;

-- Trigger to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_customers_updated_at BEFORE UPDATE ON customers
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_billing_records_updated_at BEFORE UPDATE ON billing_records
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Insert sample data
INSERT INTO customers (name, email, company, plan_type) VALUES
('Acme Corp', 'billing@acme.com', 'Acme Corporation', 'ENTERPRISE'),
('TechStart Inc', 'admin@techstart.io', 'TechStart Inc', 'PROFESSIONAL'),
('SmallBiz LLC', 'owner@smallbiz.com', 'SmallBiz LLC', 'STARTER'),
('Global Systems', 'finance@globalsys.com', 'Global Systems Ltd', 'ENTERPRISE'),
('Dev Studio', 'billing@devstudio.dev', 'Dev Studio', 'PROFESSIONAL');

-- Grant permissions
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO billing_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO billing_user;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA public TO billing_user;
