package hr.moremogucnosti.more_mogucnosti_backend.dto.recenzija;

import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikBasicDto;

import java.time.LocalDate;

public record RecenzijaZaHotelDto(
        Long id,
        KorisnikBasicDto korisnik,
        int ocjena,
        String tekst,
        LocalDate datum
) {
}
