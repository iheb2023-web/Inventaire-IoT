
-- 1. PRODUCT
CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    barcode VARCHAR(50) UNIQUE,
    rfid_tag VARCHAR(50) UNIQUE,
    description TEXT,
    unit_weight NUMERIC,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_product_name ON product(name);

-- 2. STOCK (Central warehouse)

CREATE TABLE stock (
    id SERIAL PRIMARY KEY,
    product_id INT NOT NULL REFERENCES product(id) ON DELETE CASCADE,
    quantity INT DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_stock_product ON stock(product_id);

-- 3. SHELF (Store shelves)
CREATE TABLE shelf (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    max_weight NUMERIC NOT NULL,
    min_threshold NUMERIC NOT NULL,
    current_weight NUMERIC DEFAULT 0
);

CREATE INDEX idx_shelf_name ON shelf(name);

-- 4. STORE_STOCK (Store inventory)
CREATE TABLE store_stock (
    id SERIAL PRIMARY KEY,
    product_id INT NOT NULL REFERENCES product(id) ON DELETE CASCADE,
    shelf_id INT NOT NULL REFERENCES shelf(id) ON DELETE CASCADE,
    quantity INT DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_store_stock_product ON store_stock(product_id);
CREATE INDEX idx_store_stock_shelf ON store_stock(shelf_id);

-- 5. DEVICE (ESP32)
CREATE TABLE device (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) CHECK (type IN ('ESP32')),
    location VARCHAR(20) CHECK (location IN ('STOCK', 'STORE')),
    ip_address VARCHAR(50)
);

CREATE INDEX idx_device_location ON device(location);

-- 6. RFID_EVENT (Critical table)
CREATE TABLE rfid_event (
    id SERIAL PRIMARY KEY,
    product_id INT NOT NULL REFERENCES product(id) ON DELETE CASCADE,
    event_type VARCHAR(20) CHECK (event_type IN ('ENTRY', 'EXIT')),
    location VARCHAR(20) CHECK (location IN ('STOCK', 'STORE')),
    esp32_id INT REFERENCES device(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index très importants (historique & temps réel)
CREATE INDEX idx_rfid_event_product ON rfid_event(product_id);
CREATE INDEX idx_rfid_event_type ON rfid_event(event_type);
CREATE INDEX idx_rfid_event_created ON rfid_event(created_at);
CREATE INDEX idx_rfid_event_device ON rfid_event(esp32_id);

-- 7. ALERT
CREATE TABLE alert (
    id SERIAL PRIMARY KEY,
    shelf_id INT NOT NULL REFERENCES shelf(id) ON DELETE CASCADE,
    alert_type VARCHAR(50) CHECK (alert_type IN ('LOW_WEIGHT')),
    status VARCHAR(20) CHECK (status IN ('OPEN', 'RESOLVED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_alert_shelf ON alert(shelf_id);
CREATE INDEX idx_alert_status ON alert(status);

-- 8. SALE
CREATE TABLE sale (
    id SERIAL PRIMARY KEY,
    product_id INT NOT NULL REFERENCES product(id),
    quantity INT NOT NULL,
    total_price NUMERIC NOT NULL,
    sold_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_sale_product ON sale(product_id);
CREATE INDEX idx_sale_date ON sale(sold_at);
