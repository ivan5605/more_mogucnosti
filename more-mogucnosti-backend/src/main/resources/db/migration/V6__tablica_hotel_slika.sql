CREATE TABLE hotel_slika (
    id_hotel_slika BIGINT PRIMARY KEY AUTO_INCREMENT,
    hotel_id BIGINT NOT NULL,
    putanja VARCHAR(255) NOT NULL,
    glavna_slika BOOLEAN NOT NULL DEFAULT false,
    FOREIGN KEY (hotel_id) REFERENCES hotel(id_hotel)
);