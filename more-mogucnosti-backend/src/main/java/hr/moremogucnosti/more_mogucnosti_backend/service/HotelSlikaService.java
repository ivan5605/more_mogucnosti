package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.HotelSlikaDto;

public interface HotelSlikaService {
    HotelSlikaDto getGlavnaSlika(Long hotelId);
}
