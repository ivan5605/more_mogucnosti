CREATE TABLE uloga (
    id_uloga BIGINT PRIMARY KEY AUTO_INCREMENT,
    naziv_uloga VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO uloga (naziv_uloga) VALUES ('ADMIN'), ('USER');

CREATE TABLE korisnik (
    id_korisnik BIGINT PRIMARY KEY AUTO_INCREMENT,
    ime VARCHAR(50) NOT NULL,
    prezime VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    lozinka VARCHAR (255) NOT NULL,
    uloga_id BIGINT NOT NULL,
    CONSTRAINT fk_korisnik_uloga
        FOREIGN KEY (uloga_id) REFERENCES uloga(id_uloga)
);