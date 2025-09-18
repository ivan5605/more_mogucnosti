package hr.moremogucnosti.more_mogucnosti_backend.dto.hotel;

import hr.moremogucnosti.more_mogucnosti_backend.dto.GradResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaResponseDto;

public record HotelPreviewDto(
        Long id,
        String naziv,
        GradResponseDto grad,
        String adresa,
        boolean parking,
        boolean wifi,
        boolean bazen,
        SlikaResponseDto glavnaSlika,
        boolean aktivan
) {
}
