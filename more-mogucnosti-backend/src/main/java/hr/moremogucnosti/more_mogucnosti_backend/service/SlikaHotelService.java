package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaHotelResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaResponseDto;

public interface SlikaHotelService {

    SlikaHotelResponseDto addSlikaHotel(Long hotelId, SlikaCreateDto createDto);

    void deleteSlikaHotel(Long idSlika);

    SlikaResponseDto setGlavna(Long idSlika);
}
