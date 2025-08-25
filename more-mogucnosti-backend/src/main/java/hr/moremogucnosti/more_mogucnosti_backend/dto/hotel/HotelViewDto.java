package hr.moremogucnosti.more_mogucnosti_backend.dto.hotel;

import hr.moremogucnosti.more_mogucnosti_backend.dto.GradResponseDto;

public record HotelViewDto(
    String naziv,
    GradResponseDto grad,
    String adresa
) {}
