package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.*;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Hotel;

import java.util.List;

public interface HotelService {
    HotelDetailsDto findDetailById(Long id);

    List<HotelPreviewDto> findAll();

    List<HotelPreviewDto> findRandom();

    Hotel loadEntity(Long id);

    HotelResponseDto createHotel(HotelCreateDto hotelDto);

    void softDeleteHotel(Long idHotel);

    HotelResponseDto updateHotel(Long idHotel, HotelUpdateDto hotelDto);

    void aktivirajHotel(Long idHotel);
}
