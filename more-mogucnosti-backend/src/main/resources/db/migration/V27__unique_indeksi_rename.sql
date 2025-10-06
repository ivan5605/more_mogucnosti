ALTER TABLE uloga RENAME INDEX naziv_uloga TO uq_uloga_naziv;

ALTER TABLE soba RENAME INDEX soba_hotel_broj TO uq_soba_hotel_broj;

ALTER TABLE recenzija DROP INDEX fk_recenzija_korisnik;