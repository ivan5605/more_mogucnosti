ALTER TABLE recenzija RENAME INDEX fk_recenzija_hotel TO ix_recenzija_hotel;
CREATE INDEX ix_recenzija_korisnik ON recenzija(korisnik_id);

ALTER TABLE korisnik RENAME INDEX fk_korisnik_uloga TO ix_korisnik_uloga;

ALTER TABLE hotel RENAME INDEX fk_hotel_grad TO ix_hotel_grad;

ALTER TABLE hotel_slika RENAME INDEX fk_hotelSlika_hotel TO ix_hotelSlika_hotel;

ALTER TABLE soba_slika RENAME INDEX fk_sobaSlika_soba TO ix_sobaSlika_soba;

ALTER TABLE rezervacija RENAME INDEX fk_rezervacija_korisnik TO ix_rezervacija_korisnik;
ALTER TABLE rezervacija RENAME INDEX fk_rezervacija_soba TO ix_rezervacija_soba;


ALTER TABLE soba RENAME INDEX fk_soba_hotel TO ix_soba_hotel;