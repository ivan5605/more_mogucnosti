CREATE TABLE rezervacija (
    id_rezervacija BIGINT PRIMARY KEY AUTO_INCREMENT,
    korisnik_id BIGINT NOT NULL,
    soba_id BIGINT NOT NULL,
    broj_osoba INT NOT NULL,
    datum_pocetak DATE NOT NULL,
    datum_kraj DATE NOT NULL,
    CONSTRAINT fk_rezervacija_korisnik
        FOREIGN KEY (korisnik_id) REFERENCES korisnik(id_korisnik),
    CONSTRAINT fk_rezervacija_soba
        FOREIGN KEY (soba_id) REFERENCES soba(id_soba)
);