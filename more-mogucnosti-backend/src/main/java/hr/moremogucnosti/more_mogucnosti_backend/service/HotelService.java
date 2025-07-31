package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.HotelDto;

import java.util.List;

public interface HotelService {
    HotelDto getHotel (Long id);
    List<HotelDto> getAllHotels ();
}
