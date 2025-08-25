package hr.moremogucnosti.more_mogucnosti_backend.dto.hotel;

import hr.moremogucnosti.more_mogucnosti_backend.dto.GradResponseDto;

public record HotelResponseDto(
        Long id,
        String naziv,
        GradResponseDto grad,
        String adresa,
        boolean parking,
        boolean wifi,
        boolean bazen
) {}
