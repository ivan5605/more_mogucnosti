package hr.moremogucnosti.more_mogucnosti_backend.dto.Slika;

import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.HotelViewDto;

public record SlikaHotelResponseDto(
        Long id,
        String putanja,
        boolean glavna,
        HotelViewDto hotel
) {
}
