CREATE TABLE soba (
   id_soba BIGINT PRIMARY KEY AUTO_INCREMENT,
   hotel_id BIGINT NOT NULL,
   cijena_nocenja DECIMAL(10,2) NOT NULL,
   kapacitet INT NOT NULL,
   broj_sobe INT NOT NULL,
   balkon BOOLEAN NOT NULL,
   pet_friendly BOOLEAN NOT NULL DEFAULT true,
   FOREIGN KEY (hotel_id) REFERENCES hotel(id_hotel)
);