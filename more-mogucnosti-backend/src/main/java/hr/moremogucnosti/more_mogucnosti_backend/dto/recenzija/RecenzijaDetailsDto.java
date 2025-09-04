package hr.moremogucnosti.more_mogucnosti_backend.dto.recenzija;

import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.HotelViewDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.korisnik.KorisnikViewDto;

import java.time.LocalDate;

public record RecenzijaDetailsDto(
        Long idRecenzija,
        KorisnikViewDto korisnikViewDto,
        HotelViewDto hotelViewDto,
        int ocjena,
        String tekst,
        LocalDate datum
) {
}
