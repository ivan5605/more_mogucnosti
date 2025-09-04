package hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija;

import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikBasicDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.soba.SobaViewDto;

import java.time.LocalDate;

public record RezervacijaDetailsDto (
    Long idRezervacija,
    KorisnikBasicDto korisnik,
    SobaViewDto soba,
    int brojOsoba,
    LocalDate datumPocetak,
    LocalDate datumKraj
) {}

//imena komponenti moraju se točno poklapati s imenima polja u entitetu

//dok sam imal klase za dto onda je spring gledaj imena gettera (getKorisnik - korisnik) pa je bilo više fleksibilnosti

//sad dok imam record za dto - nema getter-a, ime svojstva je doslovno ime komponente (korisnikDto - spring očekuje Rezervacija.korisnik.dto)
//entitet nema takvu putanju pa kod puca
//dok koristim record moram uskladiti imena entiteta i imena u record