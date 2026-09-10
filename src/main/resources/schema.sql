-- ============================================
-- Script de creation de la table et donnees de test
-- A executer sur MySQL avant de lancer le batch
-- ============================================

CREATE DATABASE IF NOT EXISTS batch_exporter_db;
USE batch_exporter_db;

CREATE TABLE IF NOT EXISTS products (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    category    VARCHAR(100)   NOT NULL,
    price       DECIMAL(10, 2) NOT NULL,
    quantity    INT            NOT NULL,
    active      BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at  DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO products (name, category, price, quantity, active, created_at) VALUES
('MacBook Pro 16"',      'Electronics', 2499.99, 50,  TRUE,  '2024-01-15 10:00:00'),
('iPhone 15 Pro',        'Electronics', 1199.00, 200, TRUE,  '2024-01-20 14:30:00'),
('AirPods Pro',          'Electronics',  249.00, 500, TRUE,  '2024-02-01 09:00:00'),
('Office Chair Ergo',    'Furniture',    599.50, 75,  TRUE,  '2024-02-10 11:00:00'),
('Standing Desk',        'Furniture',    899.00, 30,  TRUE,  '2024-03-01 08:00:00'),
('Mechanical Keyboard',  'Accessories',  159.99, 300, TRUE,  '2024-03-15 16:00:00'),
('USB-C Hub',            'Accessories',   79.99, 150, TRUE,  '2024-04-01 12:00:00'),
('Old Monitor 17"',      'Electronics',  199.99, 10,  FALSE, '2023-06-01 10:00:00'),
('Broken Mouse',         'Accessories',   29.99, 0,   FALSE, '2023-08-15 14:00:00'),
('Wireless Headphones',  'Electronics',  349.00, 120, TRUE,  '2024-04-20 10:30:00');
