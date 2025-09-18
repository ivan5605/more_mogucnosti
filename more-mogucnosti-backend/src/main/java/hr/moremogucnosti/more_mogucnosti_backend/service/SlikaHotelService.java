package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaCreateDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.Slika.SlikaHotelResponseDto;

public interface SlikaHotelService {

    SlikaHotelResponseDto addSlikaHotel(Long hotelId, SlikaCreateDto createDto);

}
