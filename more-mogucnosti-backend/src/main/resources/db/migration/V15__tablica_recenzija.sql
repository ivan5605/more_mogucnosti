  CREATE TABLE recenzija (
      id_recenzija BIGINT PRIMARY KEY AUTO_INCREMENT,
      korisnik_id BIGINT NOT NULL,
      hotel_id BIGINT NOT NULL,
      ocjena INT NOT NULL CHECK (ocjena BETWEEN 1 AND 5),
      tekst VARCHAR(250),
      datum DATE NOT NULL,
      CONSTRAINT fk_recenzija_korisnik
          FOREIGN KEY (korisnik_id) REFERENCES korisnik(id_korisnik),
      CONSTRAINT fk_recenzija_hotel
          FOREIGN KEY (hotel_id) REFERENCES hotel(id_hotel),
      CONSTRAINT uq_recenzija_korisnik_hotel
          UNIQUE (korisnik_id, hotel_id)
  );