SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE IF NOT EXISTS currencies (
    id INT PRIMARY KEY AUTO_INCREMENT,
    code CHAR(3) NOT NULL UNIQUE,
    full_name VARCHAR(255) NOT NULL,
    sign VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS exchange_rates (
    id INT PRIMARY KEY AUTO_INCREMENT,
    base_currency_id INT NOT NULL,
    target_currency_id INT NOT NULL,
    rate DECIMAL(10, 6) NOT NULL,

    UNIQUE (base_currency_id, target_currency_id),

    CONSTRAINT fk_base_currency
        FOREIGN KEY (base_currency_id)
        REFERENCES currencies(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_target_currency
        FOREIGN KEY (target_currency_id)
        REFERENCES currencies(id)
        ON DELETE CASCADE
);

SET FOREIGN_KEY_CHECKS = 1;

