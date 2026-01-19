CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    barcode VARCHAR(50) UNIQUE,
    rfid_tag VARCHAR(50) UNIQUE,
    description TEXT,
    unit_weight NUMERIC,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE stock (
    id SERIAL PRIMARY KEY,
    product_id INT REFERENCES product(id),
    quantity INT DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
