package hr.moremogucnosti.more_mogucnosti_backend.service;

import hr.moremogucnosti.more_mogucnosti_backend.dto.SlikaDto;

import java.util.List;

public interface SlikaService {
    SlikaDto getGlavnaSlikaHotel(Long hotelId);
    List<SlikaDto> getOstaleSlikeHotel(Long hotelId);
}
