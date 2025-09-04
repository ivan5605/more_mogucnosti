CREATE TABLE grad (
    id_grad BIGINT PRIMARY KEY AUTO_INCREMENT,
    ime_grad VARCHAR(50) NOT NULL
);

CREATE TABLE hotel (
    id_hotel BIGINT PRIMARY KEY AUTO_INCREMENT,
    naziv VARCHAR(75) NOT NULL,
    grad_id BIGINT NOT NULL,
    adresa VARCHAR(100) NOT NULL,
    parking BOOLEAN NOT NULL,
    wifi BOOLEAN NOT NULL,
    bazen BOOLEAN NOT NULL,
    CONSTRAINT fk_hotel_grad
        FOREIGN KEY (grad_id) REFERENCES grad(id_grad)
);