package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.HotelDetailsDto;
import hr.moremogucnosti.more_mogucnosti_backend.dto.hotel.HotelPreviewDto;
import hr.moremogucnosti.more_mogucnosti_backend.entity.Hotel;

import java.util.List;

public interface HotelService {
    HotelDetailsDto findDetailById(Long id);
    List<HotelPreviewDto> findAll();
    List<HotelPreviewDto> findRandom();
    Hotel loadEntity(Long id);
}
