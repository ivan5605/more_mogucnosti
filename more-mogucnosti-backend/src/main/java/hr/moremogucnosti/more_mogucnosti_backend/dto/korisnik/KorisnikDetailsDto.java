package hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik;

import hr.moremogucnosti.more_mogucnosti_backend.dto.recenzija.RecenzijaZaKorisnikDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.rezervacija.RezervacijaZaKorisnikDto;

import java.util.List;

public record KorisnikDetailsDto(
        Long id,
        String ime,
        String prezime,
        String email,
        List<RezervacijaZaKorisnikDto> rezervacije,
        List<RecenzijaZaKorisnikDto> recenzije
) {
}
