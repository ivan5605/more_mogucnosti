package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.HotelResponseDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Hotel;

import java.util.List;

public interface HotelService {
    HotelResponseDto getDto(Long id);
    List<HotelResponseDto> getAllHotels ();
    List<HotelResponseDto> getRandomHotels();
    Hotel getEntity(Long id);
}
