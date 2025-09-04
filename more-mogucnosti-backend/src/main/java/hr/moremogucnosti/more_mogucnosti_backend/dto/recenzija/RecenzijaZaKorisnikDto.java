package hr.moremogucnosti.more_mogucnosti_backend.dto.recenzija;

import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.HotelViewDto;

import java.time.LocalDate;

public record RecenzijaZaKorisnikDto(
        Long id,
        HotelViewDto hotel,
        int ocjena,
        String tekst,
        LocalDate datum
) {
}
